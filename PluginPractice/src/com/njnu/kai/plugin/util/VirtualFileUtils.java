package com.njnu.kai.plugin.util;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.velocity.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 16/9/3
 */
public class VirtualFileUtils {

    private static final String FILE_PROTOCOL = "file://";

    public static boolean fileExists(Project project, String fileName, String folderPath) throws IOException {
        try {
            return project.getBaseDir().findFileByRelativePath(folderPath).findFileByRelativePath(fileName).exists();
        } catch (NullPointerException e) {
            return false;
        }
    }

    public static VirtualFile createOrFindFile(Project project, String fileName, String folderPath) throws IOException {
        VirtualFile folder = createFolderIfNotExist(project, folderPath);
        return folder.findOrCreateChildData(project, fileName);
    }

//    public static VirtualFile setFileContent(Project project, VirtualFile createdFile, String code) throws IOException {
//        createdFile.setBinaryContent(code.getBytes());
//        openFileInEditor(project, createdFile);
//        return createdFile;
//    }

//    private static void openFileInEditor(Project project, VirtualFile fileWithGeneratedCode) {
//        FileEditorManager.getInstance(project).openFile(fileWithGeneratedCode, true);
//    }

    private static VirtualFile createFolderIfNotExist(Project project, String folder) throws IOException {
        VirtualFile directory = project.getBaseDir();
        folder = pathRelativeToProjectBasePath(project, folder);
        String[] folders = folder.split("/");
        for (String childFolder : folders) {
            VirtualFile childDirectory = directory.findChild(childFolder);
            if (childDirectory != null && childDirectory.isDirectory()) {
                directory = childDirectory;
            } else {
                directory = directory.createChildDirectory(project, childFolder);
            }
        }
        return directory;
    }

    public static String pathRelativeToProjectBasePath(Project project, String folder) {
        String basePath = project.getBasePath();
        if (basePath.startsWith(FILE_PROTOCOL)) {
            basePath = basePath.substring(FILE_PROTOCOL.length());
        }
        if (folder.startsWith(basePath)) {
            folder = folder.substring(basePath.length() + 1); //basePath最后不带/,所以多跳过1个字符
//            if (folder.length() > 0 && folder.charAt(0) == '/') {
//                folder = folder.substring(1);
//            }
        }
        return folder;
    }

    public static List<String> getSourceRootPathList(Project project, AnActionEvent event) {
        List<String> sourceRoots = new ArrayList<>();
        String projectPath = StringUtils.normalizePath(project.getBasePath());
        for (VirtualFile virtualFile : getModuleRootManager(event).getSourceRoots(false)) {
            sourceRoots.add(StringUtils.normalizePath(virtualFile.getPath()).replace(projectPath, ""));
        }
        return sourceRoots;
    }

    private static ModuleRootManager getModuleRootManager(AnActionEvent event) {
        return ModuleRootManager.getInstance(event.getData(LangDataKeys.MODULE));
    }

}
