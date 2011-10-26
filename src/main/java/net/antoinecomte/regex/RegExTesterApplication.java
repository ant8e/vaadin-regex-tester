/* 
   Copyright 2011 Antoine Comte

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package net.antoinecomte.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;

import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class RegExTesterApplication extends com.vaadin.Application {
	private static final long serialVersionUID = -6750809342251528767L;

	private final Panel result = new Panel("Result");

	@Override
	public void init() {
		setTheme("chameleon");
		final TextField text = new TextField();

		final TextField regex = new TextField();

		Panel mainPanel = new Panel("Simple Java regular expression tool ");
		mainPanel.setWidth("460px");
		VerticalLayout mainPanelLayout = new VerticalLayout();
		mainPanelLayout.setSpacing(true);
		mainPanelLayout.setMargin(true);
		mainPanel.setContent(mainPanelLayout);
		regex.setCaption("Regular Expression");
		regex.setWidth("400px");
		regex.addStyleName("big");
		regex.setInputPrompt("enter a regular expression here");
		regex.setTextChangeEventMode(TextChangeEventMode.TIMEOUT);
		regex.setImmediate(true);

		text.setCaption("Test input");
		text.setInputPrompt("Enter a test string here");
		text.setTextChangeEventMode(TextChangeEventMode.TIMEOUT);
		text.setImmediate(true);
		text.setWidth("400px");
		text.addStyleName("big");
		result.setSizeUndefined();
		result.setWidth("460px");
		result.setStyleName("light");
		result.setVisible(false);

		regex.addListener(new TextChangeListener() {
			private static final long serialVersionUID = 7783333579512074097L;

			@Override
			public void textChange(TextChangeEvent event) {
				showResult(event.getText(), text.getValue().toString());

			}
		});
		text.addListener(new TextChangeListener() {
			private static final long serialVersionUID = -2294521048305268959L;

			@Override
			public void textChange(TextChangeEvent event) {
				showResult(regex.getValue().toString(), event.getText());
			}
		});
		Window mainWindow = new Window();
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);
		mainLayout.setMargin(true);
		mainWindow.setContent(mainLayout);
		mainWindow.addComponent(mainPanel);
		mainPanel.addComponent(regex);
		mainPanel.addComponent(text);
		mainWindow.addComponent(result);
		setMainWindow(mainWindow);
	}

	private void showResult(String regexValue, String textValue) {
		Matcher matcher;
		try {
			result.setVisible(!"".equals(regexValue));
			Label match = new Label("no match");
			match.addStyleName("h3 color");
			result.removeAllComponents();
			result.addComponent(match);
			matcher = Pattern.compile(regexValue).matcher(textValue);
			if (matcher.matches()) {
				if (matcher.groupCount() > 0)
					for (int i = 1; i <= matcher.groupCount(); i++) {
						Label g = new Label("group " + i + " = " + matcher.group(i));
						g.addStyleName("h3 color");
						g.setSizeUndefined();
						result.addComponent(g);
					}
				match.setValue("match");
			}
			matcher.reset();
			if (matcher.find()) {
				Label findresult = new Label("find=true, start = " +
						matcher.start() + " end = " + matcher.end());
				findresult.addStyleName("h3 color");
				result.addComponent(findresult);
			}
			Label javaString = new Label("java string : \""
					+ StringEscapeUtils.escapeJava(regexValue) + "\"");
			javaString.addStyleName("small color");
			result.addComponent(javaString);
		} catch (Exception e) {
			result.removeAllComponents();
			Label error = new Label(e.getMessage());
			error.addStyleName("error");
			result.addComponent(error);
		}
	}
}
