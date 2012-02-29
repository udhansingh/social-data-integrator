package org.onesun.commons.webbrowser;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.CloseWindowListener;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.browser.VisibilityWindowListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class EmbeddedWebBrowser implements WebBrowser {
	private static Logger logger = Logger.getLogger(EmbeddedWebBrowser.class);
	
	private String title = null;
	private Display display = null;
	private Shell shell = null;
	private Browser browser = null;
	private LocationChangeHandler locationChangeHandler = null;
	
	public LocationChangeHandler getLocationChangeHandler() {
		return locationChangeHandler;
	}

	public void setLocationChangeHandler(LocationChangeHandler locationChangeHandler) {
		this.locationChangeHandler = locationChangeHandler;
	}

	public EmbeddedWebBrowser(String title, LocationChangeHandler locationChangeHandler){
		this.title = title;
		this.setLocationChangeHandler(locationChangeHandler);
	}
	
	private void init(final Display display, Browser browser) {
		browser.setJavascriptEnabled(true);
		
		browser.addOpenWindowListener(new OpenWindowListener() {
			public void open(WindowEvent event) {
				if (!event.required) return;	/* only do it if necessary */
				Shell shell = new Shell(display, SWT.SHELL_TRIM);
				shell.setText("New Window");
				shell.setLayout(new FillLayout());
				Browser browser = new Browser(shell, SWT.NONE);
				init(display, browser);
				event.browser = browser;
			}
		});
		
		browser.addLocationListener(new LocationListener() {
			@Override
			public void changing(LocationEvent event) {
			}
			
			@Override
			public void changed(LocationEvent event) {
				logger.info("Location Changed: " + event.location);
				onLocationChanged(event.location);
			}
		});
		
		browser.addVisibilityWindowListener(new VisibilityWindowListener() {
			public void hide(WindowEvent event) {
				Browser browser = (Browser)event.widget;
				Shell shell = browser.getShell();
				shell.setVisible(false);
			}
			public void show(WindowEvent event) {
				Browser browser = (Browser)event.widget;
				final Shell shell = browser.getShell();
				if (event.location != null) shell.setLocation(event.location);
				if (event.size != null) {
					Point size = event.size;
					shell.setSize(shell.computeSize(size.x, size.y));
				}
				shell.open();
			}
		});
		
		browser.addCloseWindowListener(new CloseWindowListener() {
			public void close(WindowEvent event) {
				Browser browser = (Browser)event.widget;
				Shell shell = browser.getShell();
				shell.close();
			}
		});
		
		browser.addStatusTextListener(new StatusTextListener() {
			@Override
			public void changed(StatusTextEvent event) {
				logger.info(event);
			}
		});
	}
	
	class AsyncExecutor implements Runnable{
		private String url = null;
		
		AsyncExecutor(String url){
			this.url = url;
		}
		
		@Override
		public void run() {
			if(browser.isDisposed()){
				return;
			}
			else {
				browser.setUrl(url);
			}
		}
	}
	
	@Override
	public void browse(String url){
		display = new Display();
		display.asyncExec(new AsyncExecutor(url));
		
		shell = new Shell(display, SWT.SHELL_TRIM);
		shell.setText(title);
		shell.setLayout(new FillLayout());
		
		try {
			Browser.clearSessions();
			browser = new Browser(shell, SWT.NONE);
		} catch (SWTError e) {
			logger.info("Could not instantiate Browser: " + e.getMessage());
			display.dispose();
			return;
		}
		
		init(display, browser);
		
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	@Override
	public void onLocationChanged(String url) {
		if(locationChangeHandler != null){
			boolean flag = locationChangeHandler.execute(url);

			if(flag == true){
				display.dispose();
			}
		}
	}
}
