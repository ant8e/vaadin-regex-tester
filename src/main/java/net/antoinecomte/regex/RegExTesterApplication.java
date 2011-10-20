package net.antoinecomte.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

public class RegExTesterApplication extends com.vaadin.Application {
	private static final long serialVersionUID = -6750809342251528767L;
	private final TextField text = new TextField();;
	private final TextField regex = new TextField();;
	private final Label match = new Label("match");

	@Override
	public void init() {
		Panel l = new Panel("Simple Java RegEx Tester ");
		regex.setCaption("Regular Expression");
		regex.setWidth("300px");
		regex.setInputPrompt("enter a regular expression here");
		regex.setTextChangeEventMode(TextChangeEventMode.TIMEOUT);
		regex.setImmediate(true);

		text.setCaption("Text");
		text.setInputPrompt("Enter a text here");
		text.setTextChangeEventMode(TextChangeEventMode.TIMEOUT);
		text.setImmediate(true);
		text.setWidth("300px");

		match.setVisible(false);
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
		mainWindow.addComponent(l);
		l.addComponent(regex);
		l.addComponent(text);
		l.addComponent(match);
		setMainWindow(mainWindow);
	}

	private void showResult(String regexValue, String textValue) {
		Matcher matcher;
		try {
			match.setVisible(false);
			matcher = Pattern.compile(regexValue).matcher(textValue);
			if (matcher.matches()) {
				match.setVisible(true);
				String groups = "";
				if (matcher.groupCount() > 0)
					for (int i = 1; i <= matcher.groupCount(); i++) {
						groups = groups + " group" + i + "=" + matcher.group(i);
					}
				match.setValue("match" + groups);

			}
		} catch (Exception e) {
		}

	}
}
