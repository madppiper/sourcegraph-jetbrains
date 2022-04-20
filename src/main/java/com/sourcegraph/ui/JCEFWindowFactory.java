package com.sourcegraph.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;

public class JCEFWindowFactory implements ToolWindowFactory {
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        JCEFWindow myToolWindow = new JCEFWindow(toolWindow, project);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(myToolWindow.getContent(), "Sourcegraph", false);
        toolWindow.getContentManager().addContent(content);
    }
}
