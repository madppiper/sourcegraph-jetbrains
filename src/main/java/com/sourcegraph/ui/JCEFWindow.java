package com.sourcegraph.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.jcef.JBCefBrowser;
import com.intellij.ui.jcef.JBCefBrowserBase;
import com.intellij.ui.jcef.JBCefJSQuery;
import com.sourcegraph.util.SourcegraphUtil;
import org.cef.browser.CefBrowser;

import javax.swing.*;
import java.awt.*;

public class JCEFWindow {
    private JPanel panel;
    private Project project;
    private ToolWindow toolWindow;
    private JBCefBrowserBase browser = new JBCefBrowser(SourcegraphUtil.TEST_URL);;
    private JBCefJSQuery query;

    public JCEFWindow(ToolWindow toolWindow, Project project) {
        this.toolWindow = toolWindow;
        this.project = project;
        panel = new JPanel(new BorderLayout());
        //browser.openDevtools();
        JBCefJSQuery myQueryInBrowser = JBCefJSQuery.create((JBCefBrowserBase) browser);
        myQueryInBrowser.addHandler((link) -> {
            return null;
        });




        CefBrowser cefBrowser = browser.getCefBrowser();

        cefBrowser.executeJavaScript(
                "window.JavaPanelBridge = {" +
                        "openInExternalBrowser : function(link) {" +
                        myQueryInBrowser.inject("link") +
                        "}" +
                        "};",
                cefBrowser.getURL(), 0);

        // Dispose the query when necessary
        Disposer.dispose(myQueryInBrowser);



        panel.add(browser.getComponent(),BorderLayout.CENTER);

    }

    public JPanel getContent() {
        return panel;
    }

}
