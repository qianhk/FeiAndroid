package com.njnu.kai.plugin.mapper.processor;

import com.intellij.psi.PsiField;

/**
 * Created by kai
 * since 17/4/14
 */
public interface MapperPoClassListener {

    void notifyFoundFieldInPoClass(PsiField field);

    void notifyFoundFieldInPoClass(String poCanonicalText);
}
