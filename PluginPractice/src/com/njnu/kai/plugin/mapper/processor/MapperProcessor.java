package com.njnu.kai.plugin.mapper.processor;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.search.GlobalSearchScope;
import com.njnu.kai.plugin.mapper.MapperRuntimeParams;
import com.njnu.kai.plugin.mapper.model.WaitPOItem;
import com.njnu.kai.plugin.mapper.model.WaitPOManager;
import com.njnu.kai.plugin.util.Utils;

public class MapperProcessor extends WriteCommandAction.Simple implements MapperPoClassListener {

    private MapperRuntimeParams mMapperRuntimeParams;

    public MapperProcessor(final MapperRuntimeParams params) {
        super(params.getProject(), "po-to-vo-mapper-command");
        mMapperRuntimeParams = params;
        final WaitPOItem waitPOItem = new WaitPOItem();
        waitPOItem.setPoClass(params.getOriginClass());
        waitPOItem.setVoClassCanonicalName(params.getVoClassCanonicalName());
        waitPOItem.setMapperClassCanonicalName(params.getMapperClassCanonicalName());
        WaitPOManager.getInstance().push(waitPOItem);
    }

    @Override
    protected void run() {
        final WaitPOManager instance = WaitPOManager.getInstance();
        int index = 0;
        while (!instance.empty()) {
            final WaitPOItem waitPOItem = instance.pop();
            if (waitPOItem != null) {
                ++index;
                new MapperPoClassByField(getProject(), this, waitPOItem).execute(index > 1);
            }
        }
        instance.clear();
    }

    @Override
    public void notifyFoundFieldInPoClass(PsiField field) {
        notifyFoundFieldInPoClass(field.getType().getCanonicalText());
    }

    @Override
    public void notifyFoundFieldInPoClass(String poCanonicalText) {
        if (mMapperRuntimeParams.isCascadeMapper() && !Utils.isJavaInnerClass(poCanonicalText)) {
            final Project project = getProject();
            JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(project);
            GlobalSearchScope searchScope = GlobalSearchScope.allScope(project);
            final PsiClass typePoClass = javaPsiFacade.findClass(poCanonicalText, searchScope);
            if (typePoClass != null) {
                final WaitPOItem waitPOItem = new WaitPOItem();
                waitPOItem.setPoClass(typePoClass);
                String voClassFullName = Utils.replaceFullPkgWithGivenClass(mMapperRuntimeParams.getVoClassCanonicalName(), typePoClass.getName()) + "VO";
                waitPOItem.setVoClassCanonicalName(voClassFullName);
                waitPOItem.setMapperClassCanonicalName(mMapperRuntimeParams.getMapperClassCanonicalName());
                WaitPOManager.getInstance().push(waitPOItem);
            } else {
                System.err.println("notifyFoundFieldInPoClass but not found class: " + poCanonicalText);
            }
        }
    }
}
