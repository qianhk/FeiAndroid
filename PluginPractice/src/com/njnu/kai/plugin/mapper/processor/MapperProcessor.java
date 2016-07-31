package com.njnu.kai.plugin.mapper.processor;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiType;
import com.intellij.psi.search.GlobalSearchScope;
import com.njnu.kai.plugin.mapper.TmpRuntimeParams;
import com.njnu.kai.plugin.mapper.model.WaitPOItem;
import com.njnu.kai.plugin.mapper.model.WaitPOManager;
import com.njnu.kai.plugin.util.Utils;

public class MapperProcessor extends WriteCommandAction.Simple implements MapperPoClass.MapperPoClassListener {

    private TmpRuntimeParams mTmpRuntimeParams;

    public MapperProcessor(final TmpRuntimeParams params) {
        super(params.getProject(), "po-to-vo-mapper-command");
        mTmpRuntimeParams = params;
        final WaitPOItem waitPOItem = new WaitPOItem();
        waitPOItem.setPoClass(params.getOriginClass());
        waitPOItem.setVoClassCanonicalName(params.getVoClassCanonicalName());
        waitPOItem.setMapperClassCanonicalName(params.getMapperClassCanonicalName());
        WaitPOManager.getInstance().push(waitPOItem);
    }

    @Override
    protected void run() {
        final WaitPOManager instance = WaitPOManager.getInstance();
        while (!instance.empty()) {
            final WaitPOItem waitPOItem = instance.pop();
            if (waitPOItem != null) {
                new MapperPoClass(getProject(), this, waitPOItem).execute();
            }
        }
        instance.clear();
    }

    @Override
    public void notifyFoundFieldInPoClass(PsiField field) {
        final String name = field.getName(); //getType PsiClassreferenceType //PsiJavaCodeReferenceElement
        if (name.endsWith("PO")) {
//            System.out.println("field po: " + field + " name=" + name);
            final PsiType type = field.getType();
            final String canonicalText = type.getCanonicalText();
            final Project project = getProject();
            JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(project);
            GlobalSearchScope searchScope = GlobalSearchScope.allScope(project);
            final PsiClass typePoClass = javaPsiFacade.findClass(canonicalText, searchScope);
            if (typePoClass != null) {
                final WaitPOItem waitPOItem = new WaitPOItem();
                waitPOItem.setPoClass(typePoClass);
                //todo get new po to vo
                String voClassFullName = Utils.replaceFullPkgWithGivenClass(mTmpRuntimeParams.getVoClassCanonicalName(), typePoClass.getName()) + "VO";
                waitPOItem.setVoClassCanonicalName(voClassFullName);
                waitPOItem.setMapperClassCanonicalName(mTmpRuntimeParams.getMapperClassCanonicalName());
                WaitPOManager.getInstance().push(waitPOItem);
            }
        }
    }
}
