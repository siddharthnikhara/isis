package org.nakedobjects.viewer.skylark.metal;

import org.nakedobjects.viewer.skylark.Bounds;
import org.nakedobjects.viewer.skylark.Canvas;
import org.nakedobjects.viewer.skylark.Click;
import org.nakedobjects.viewer.skylark.Location;
import org.nakedobjects.viewer.skylark.Size;
import org.nakedobjects.viewer.skylark.View;
import org.nakedobjects.viewer.skylark.core.AbstractBorder;

import java.awt.event.KeyEvent;


public class ButtonBorder extends AbstractBorder {
    private static final int BUTTON_SPACING = 5;
    private final Button[] buttons;
    private ButtonAction defaultAction;

    public ButtonBorder(ButtonAction[] actions, View view) {
        super(view);

        buttons = new Button[actions.length];
        for (int i = 0; i < actions.length; i++) {
            ButtonAction action = actions[i];
            buttons[i] = new Button(action, view);
            if (action.isDefault()) {
                defaultAction = action;
            }
        }
        // space for: line & button with whitspace
        bottom = 1 + VPADDING + buttons[0].getRequiredSize().getHeight() + VPADDING;

    }

    public void draw(Canvas canvas) {
  //      int width = getSize().getWidth();
  //      int y = getSize().getHeight() - bottom;

   //     canvas.clearBackground(this, Style.SECONDARY3);
        
        // draw dividing line
//        canvas.drawLine(0, y, width, y, Style.SECONDARY1);

        // draw buttons
        for (int i = 0; i < buttons.length; i++) {
            Canvas buttonCanvas = canvas.createSubcanvas(buttons[i].getBounds());
            buttons[i].draw(buttonCanvas);
            int buttonWidth = buttons[i].getSize().getWidth();
            buttonCanvas.offset(BUTTON_SPACING + buttonWidth, 0);
        }
        
        // draw rest
        super.draw(canvas);
    }

    public void firstClick(Click click) {
        View button = overButton(click.getLocation());
        if (button == null) {
            super.firstClick(click);
        } else {
            button.firstClick(click);
        }
    }

    public Size getRequiredSize() {
        Size size = super.getRequiredSize();
        size.ensureWidth(totalButtonWidth());
        return size;
    }

    public View identify(Location location) {
        for (int i = 0; i < buttons.length; i++) {
            Button button = buttons[i];
            if (button.getBounds().contains(location)) {
                return button;
            }
        }
        return super.identify(location);
    }

    public void keyPressed(final int keyCode, final int modifiers) {
        if (keyCode == KeyEvent.VK_ENTER) {
            if (defaultAction != null && defaultAction.disabled(getView()).isAllowed()) {
                defaultAction.execute(getWorkspace(), getView(), getLocation());
            }
        }

        super.keyPressed(keyCode, modifiers);
    }

    public void layout(int width) {
        int x = width / 2 - totalButtonWidth() / 2;
        int y = getSize().getHeight() - VPADDING - buttons[0].getRequiredSize().getHeight();

        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = buttons[i];
            buttons[i].setSize(buttons[i].getRequiredSize());
            buttons[i].setLocation(new Location(x, y));

            x += buttons[i].getSize().getWidth();
            x += BUTTON_SPACING;
        }
    }

    public void mouseDown(Click click) {
        View button = overButton(click.getLocation());
        if (button == null) {
            super.mouseDown(click);
        } else {
            button.mouseDown(click);
        }
    }

    public void mouseUp(Click click) {
        View button = overButton(click.getLocation());
        if (button == null) {
            super.mouseUp(click);
        } else {
            button.mouseUp(click);
        }
    }

    /**
     * Finds the action button under the pointer; returning null if none.
     */
    private View overButton(Location location) {
        for (int i = 0; i < buttons.length; i++) {
            Button button = buttons[i];
            if (button.getBounds().contains(location)) {
                return button;
            }
        }

        return null;
    }

    public void secondClick(Click click) {
        View button = overButton(click.getLocation());
        if (button == null) {
            super.secondClick(click);
        }
    }

    public void setBounds(Bounds bounds) {
        super.setBounds(bounds);
        layout(bounds.getWidth());
    }

    public void setSize(Size size) {
        super.setSize(size);
        layout(size.getWidth());
    }

    public void thirdClick(Click click) {
        View button = overButton(click.getLocation());
        if (button == null) {
            super.thirdClick(click);
        }
    }

    private int totalButtonWidth() {
        int totalButtonWidth = 0;
        for (int i = 0; i < buttons.length; i++) {
            int buttonWidth = buttons[i].getRequiredSize().getWidth();
            totalButtonWidth += i > 0 ? BUTTON_SPACING : 0;
            totalButtonWidth += buttonWidth;
        }
        return totalButtonWidth;
    }

}

/*
 * Naked Objects - a framework that exposes behaviourally complete business objects directly to the user.
 * Copyright (C) 2000 - 2005 Naked Objects Group Ltd
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation; either version 2 of the License, or (at your
 * option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if not, write to
 * the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * The authors can be contacted via www.nakedobjects.org (the registered address of Naked Objects Group is
 * Kingsway House, 123 Goldworth Road, Woking GU21 1NR, UK).
 */