package com.njnu.kai.plugin.viewgenerator.iface;

import com.njnu.kai.plugin.viewgenerator.model.Element;
import com.njnu.kai.plugin.viewgenerator.model.VGContext;

import java.util.ArrayList;

public interface IConfirmListener {
    public void onConfirm(VGContext context, ArrayList<Element> elements, String fieldNamePrefix);
}
