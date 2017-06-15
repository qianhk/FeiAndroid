package com.njnu.kai.plugin.mvp;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiPackage;
import com.intellij.psi.impl.file.PsiJavaDirectoryImpl;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.GlobalSearchScopeUtil;
import com.intellij.util.indexing.FileBasedIndex;
import com.njnu.kai.plugin.mvp.processor.TemplateMvpProcessor;
import com.njnu.kai.plugin.util.VirtualFileUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by kai
 * since 16/8/31
 */
public class TemplateMvpGenerateAction extends AnAction {

    private static final String BOUND_X = "mvp_bound_x";
    private static final String BOUND_Y = "mvp_bound_y";
    private static final String BOUND_WIDTH = "mvp_bound_width";
    private static final String BOUND_HEIGHT = "mvp_bound_height";

    private TemplateMvpChoiceDialog mDialog;

    @Override
    public void actionPerformed(AnActionEvent event) {

        final Project project = event.getData(PlatformDataKeys.PROJECT);
        PsiJavaDirectoryImpl psiJavaDirectory = (PsiJavaDirectoryImpl) event.getData(PlatformDataKeys.PSI_ELEMENT);
        String dirPath = psiJavaDirectory.getVirtualFile().getCanonicalPath();
        String srcRelativePath = VirtualFileUtils.pathRelativeToProjectBasePath(project, dirPath);
        JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(project);

        String choosePackage = srcRelativePath.replace('/', '.');
        PsiPackage aPackage = javaPsiFacade.findPackage(choosePackage);

        int pos = choosePackage.indexOf('.');
        for (; aPackage == null && pos >= 0; ) {
            choosePackage = choosePackage.substring(pos + 1);
            aPackage = javaPsiFacade.findPackage(choosePackage);
            pos = choosePackage.indexOf('.');
        }
        String qualifiedName = aPackage.getQualifiedName();

        final MvpRuntimeParams params = new MvpRuntimeParams(new com.njnu.kai.plugin.mvp.MvpRuntimeParams.Action() {
            @Override
            public void run(com.njnu.kai.plugin.mvp.MvpRuntimeParams params) {
                final Rectangle bounds = mDialog.getBounds();
                PropertiesComponent.getInstance().setValue(BOUND_X, bounds.x, 100);
                PropertiesComponent.getInstance().setValue(BOUND_Y, bounds.y, 100);
                PropertiesComponent.getInstance().setValue(BOUND_WIDTH, bounds.width, 800);
                PropertiesComponent.getInstance().setValue(BOUND_HEIGHT, bounds.height, 300);
                new TemplateMvpProcessor(params).execute();
            }
        }, project);
        String presentationPkg = qualifiedName + ".presentation";
        String viewPkg = presentationPkg + ".view";
        params.setListActivityPackageName(viewPkg + ".activity");
        params.setListFragmentPackageName(viewPkg + ".fragment");
        params.setListAdapterPackageName(viewPkg + ".adapter");
        params.setListPresenterPackageName(presentationPkg + ".presenter");
        params.setNuwaBinderPackageName(viewPkg + ".nuwabinder");
        params.setNuwaVOPackageName(viewPkg + ".nuwavo");
        params.setListVoPackageName(presentationPkg + ".vo");

        VirtualFile manifestFile = null;
        Module module = ModuleUtil.findModuleForPsiElement(psiJavaDirectory);
        if (module != null) {
            FileBasedIndex instance = FileBasedIndex.getInstance();
            GlobalSearchScope allScope = GlobalSearchScope.allScope(project); //ProjectAndLibrariesScope
            GlobalSearchScope everythingScope = GlobalSearchScope.everythingScope(project); //ProjectScope$5$1
            Collection<VirtualFile> fileList = FilenameIndex.getVirtualFilesByName(project, "AndroidManifest.xml", allScope); //size=79
            Collection<VirtualFile> fileList2 = FilenameIndex.getVirtualFilesByName(project, "AndroidManifest.xml", everythingScope); //size=79

            GlobalSearchScope moduleContentScope = module.getModuleContentScope(); //ModuleWithDependenciesScope
            Collection<VirtualFile> fileList3 = FilenameIndex.getVirtualFilesByName(project, "AndroidManifest.xml", moduleContentScope); //size=25
            if (fileList3 != null && fileList3.size() > 0) {
                ArrayList<VirtualFile> manifestFileList = new ArrayList<>();
                for (VirtualFile virtualFile : fileList3) {
                    String canonicalPath = virtualFile.getCanonicalPath();
                    if (!canonicalPath.contains("/build/")) {
                        manifestFileList.add(virtualFile);
                    }
                }
                if (manifestFileList.size() > 0) {
                    manifestFile = manifestFileList.get(0);
                }
            }

            GlobalSearchScope moduleRuntimeScope = module.getModuleRuntimeScope(false); //ModuleWithDependenciesScope
            Collection<VirtualFile> fileList4 = FilenameIndex.getVirtualFilesByName(project, "AndroidManifest.xml", moduleRuntimeScope); //size=1 jar:///AndroidDev/android-sdk-macosx/platforms/android-23/android.jar!/AndroidManifest.xml


            GlobalSearchScope moduleContentWithDependenciesScope = module.getModuleContentWithDependenciesScope(); //ModuleWithDependenciesScope
            Collection<VirtualFile> fileList5 = FilenameIndex.getVirtualFilesByName(project, "AndroidManifest.xml", moduleContentWithDependenciesScope);//size=45

            GlobalSearchScope moduleScope = module.getModuleScope(false); //ModuleWithDependenciesScope
            Collection<VirtualFile> fileList6 = FilenameIndex.getVirtualFilesByName(project, "AndroidManifest.xml", moduleScope); //size=0

            GlobalSearchScope moduleWithDependentsScope = module.getModuleWithDependentsScope(); //ModuleWithDependentsScope
            Collection<VirtualFile> fileList7 = FilenameIndex.getVirtualFilesByName(project, "AndroidManifest.xml", moduleWithDependentsScope); //size=25

            GlobalSearchScope moduleWithDependenciesAndLibrariesScope = module.getModuleWithDependenciesAndLibrariesScope(false); //ModuleWithDependenciesScope
            Collection<VirtualFile> fileList8 = FilenameIndex.getVirtualFilesByName(project, "AndroidManifest.xml", moduleWithDependenciesAndLibrariesScope); //size=1 jar:///AndroidDev/android-sdk-macosx/platforms/android-23/android.jar!/AndroidManifest.xml
        }

        mDialog = new TemplateMvpChoiceDialog(params);

        mDialog.setBounds(PropertiesComponent.getInstance().getInt(BOUND_X, 100), PropertiesComponent.getInstance().getInt(BOUND_Y, 100)
                , PropertiesComponent.getInstance().getInt(BOUND_WIDTH, 800), PropertiesComponent.getInstance().getInt(BOUND_HEIGHT, 300));
        mDialog.setVisible(true);
    }

}
