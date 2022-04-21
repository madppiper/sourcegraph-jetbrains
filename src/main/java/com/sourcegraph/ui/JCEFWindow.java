package com.sourcegraph.ui;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.jcef.JBCefApp;
import com.intellij.ui.jcef.JBCefBrowser;
import com.intellij.ui.jcef.JBCefBrowserBase;
import com.intellij.ui.jcef.JBCefJSQuery;
import com.sourcegraph.util.SourcegraphUtil;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.handler.CefLoadHandler;
import org.cef.network.CefRequest;

import javax.swing.*;
import java.awt.*;

public class JCEFWindow {
    private final Logger logger = Logger.getInstance(this.getClass());
    private JPanel panel;
    private Project project;
    private ToolWindow toolWindow;
    private JBCefBrowserBase browser = new JBCefBrowser(SourcegraphUtil.TEST_URL);
    private CefBrowser cefBrowser = browser.getCefBrowser();
    private JBCefJSQuery query;

    public JCEFWindow(ToolWindow toolWindow, Project project) {
        this.toolWindow = toolWindow;
        this.project = project;
        panel = new JPanel(new BorderLayout());
        if (!JBCefApp.isSupported()) {
            JLabel warningLabel = new JLabel("Unfortunately, the browser is not available on your system. Try running the IDE with the default OpenJDK.");
            panel.add(warningLabel);
            return;
        }
        panel.add(browser.getComponent(),BorderLayout.CENTER);


        //Define javascript callback (here for function "test(obj)" -> we will bind them onLoad
        JBCefJSQuery myQueryInBrowser = JBCefJSQuery.create((JBCefBrowserBase) browser);
        myQueryInBrowser.addHandler((link) -> {
            logger.info(""+link);
            return null;
        });

        // Add Loadhandler
        browser.getJBCefClient().addLoadHandler(new CefLoadHandler() {
            @Override
            public void onLoadingStateChange(CefBrowser cefBrowser, boolean isLoading, boolean canGoBack, boolean canGoForward) {


            }
            @Override
            public void onLoadStart(CefBrowser cefBrowser, CefFrame frame, CefRequest.TransitionType transitionType) {

            }
            @Override
            public void onLoadEnd(CefBrowser cefBrowser, CefFrame frame, int httpStatusCode) {
                cefBrowser.executeJavaScript(
                        "window.JavaPanelBridge = {" +
                                "test : function(link) {" +
                                myQueryInBrowser.inject("link") +
                                "}" +
                                "};" ,
                        cefBrowser.getURL(), 0);
                // Dispose the query when necessary
                //Disposer.dispose(myQueryInBrowser);
            }
            @Override
            public void onLoadError(CefBrowser cefBrowser, CefFrame frame, ErrorCode errorCode, String errorText, String failedUrl) {
                System.out.println("JBCefLoadHtmlTest.onLoadError: " + failedUrl);
            }
        }, cefBrowser);

    }

    public JPanel getContent() {
        return panel;
    }

}
