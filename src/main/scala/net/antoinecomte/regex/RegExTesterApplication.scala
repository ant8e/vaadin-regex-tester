/* 
   Copyright 2011 Antoine Comte

   Licensed under the Apache License, Version 2.0 (the "License")
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package net.antoinecomte.regex

import java.util.regex.Matcher
import java.util.regex.Pattern

import org.apache.commons.lang3.StringEscapeUtils

import com.vaadin.event.FieldEvents.TextChangeEvent
import com.vaadin.event.FieldEvents.TextChangeListener
import com.vaadin.ui.AbstractTextField.TextChangeEventMode
import com.vaadin.ui.Label
import com.vaadin.ui.Panel
import com.vaadin.ui.TextField
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Window

class RegExTesterApplication extends com.vaadin.Application {

  @Override
  def init() {
    setTheme("chameleon")

    val mainPanel = new Panel("Simple Java regular expression tool") {
      setWidth("460px")
    }

    mainPanel.setContent(new VerticalLayout() {
      setSpacing(true)
      setMargin(true)
    })

    trait BigTextField extends TextField {
      setWidth("400px")
      addStyleName("big")
      setTextChangeEventMode(TextChangeEventMode.TIMEOUT)
      setImmediate(true)
    }

    val regex = new BigTextField() {
      setCaption("Regular Expression")
      setInputPrompt("enter a regular expression here")
    }

    val text = new BigTextField() {
      setCaption("Test input")
      setInputPrompt("Enter a test string here")
    }

    val result = new Panel("Result") {
      setWidth("460px")
      setStyleName("light")
      setVisible(false)
    }
    class Mylistener(val handler: (String) => Unit) extends TextChangeListener {
      def textChange(event: TextChangeEvent) {
        handler(event.getText())
      }
    }

    val mainLayout = new VerticalLayout() {
      setSpacing(true)
      setMargin(true)
    }

    val mainWindow = new Window() {
      setContent(mainLayout)
      addComponent(mainPanel)
    }
    regex.addListener(new Mylistener(showResult(_, text.getValue().toString())))
    text.addListener(new Mylistener(showResult(regex.getValue().toString(), _)))

    mainPanel.addComponent(regex)
    mainPanel.addComponent(text)
    mainWindow.addComponent(result)
    setMainWindow(mainWindow)

    def showResult(regexValue: String, textValue: String) {
      try {
        result.setVisible("" != (regexValue))
        result.removeAllComponents()
        val matchLabel = new Label("no match") {
          addStyleName("h3 color")
        }
        result.addComponent(matchLabel)
        var matcher = Pattern.compile(regexValue).matcher(textValue)
        if (matcher.matches()) {
          if (matcher.groupCount() > 0)
            for (i <- 1 to matcher.groupCount()) {
              result.addComponent(new Label("group " + i + " = " + matcher.group(i)) {
                addStyleName("h3 color")
                setSizeUndefined()
              })
            }
          matchLabel.setValue("match")
        }
        matcher.reset()
        if (matcher.find()) {
          result.addComponent(new Label("find=true, start = " +
            matcher.start() + " end = " + matcher.end()) {
            addStyleName("h3 color")
          })
        }
        result.addComponent(new Label("java string : \""
          + StringEscapeUtils.escapeJava(regexValue) + "\"") {
          addStyleName("small color")
        })
      } catch {
        case e: Exception => {
          result.removeAllComponents()
          result.addComponent(new Label(e.getMessage()) {
            addStyleName("error")
          })
        }
      }
    }
  }
}

