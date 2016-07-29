package com.njnu.kai.plugin.mapper.processor;

import com.intellij.openapi.command.WriteCommandAction;
import com.njnu.kai.plugin.mapper.TmpRuntimeParams;
import com.njnu.kai.plugin.mapper.model.WaitPOItem;
import com.njnu.kai.plugin.mapper.model.WaitPOManager;

public class MapperProcessor extends WriteCommandAction.Simple implements MapperPoClass.MapperPoClassListener {

    public MapperProcessor(final TmpRuntimeParams params) {
        super(params.getProject(), "po-to-vo-mapper-command");

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
    public void notifyFoundNewPoClass() {

    }

}
