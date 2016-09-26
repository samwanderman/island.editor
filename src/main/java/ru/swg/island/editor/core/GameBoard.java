/**
 * @author Potapov Sergei (sam-wanderman@yandex.ru)
 */
package ru.swg.island.editor.core;

import java.io.IOException;

import ru.swg.wheelframework.event.Events;
import ru.swg.wheelframework.event.event.KeyEvent;
import ru.swg.wheelframework.event.event.MouseEvent;
import ru.swg.wheelframework.event.event.SyncEvent;
import ru.swg.wheelframework.event.interfaces.KeyEventInterface;
import ru.swg.wheelframework.event.interfaces.MouseEventInterface;
import ru.swg.wheelframework.event.interfaces.SyncEventInterface;
import ru.swg.wheelframework.event.listener.KeyEventListener;
import ru.swg.wheelframework.event.listener.MouseEventListener;
import ru.swg.wheelframework.event.listener.SyncEventListener;
import ru.swg.wheelframework.view.ui.GuiImage;

/**
 * Simple GameBoard
 */
public class GameBoard extends GuiImage implements MouseEventInterface, SyncEventInterface, KeyEventInterface {
	private final int MOUSE_DETECT_X_OFFSET = 40;
	private final int MOUSE_DETECT_Y_OFFSET = 40;
	private final int MAP_SPEED = 4;
	private final int KEY_SPEED = 10;
	
	private final MouseEventListener mouseEventListener = new MouseEventListener(this);
	private final SyncEventListener syncEventListener = new SyncEventListener(this);
	private final KeyEventListener keyEventListener = new KeyEventListener(this);
	
	private int dx = 0, dy = 0, speed = 0;
	
	public GameBoard(final String path) 
			throws IOException {
		super(path);
	}
	
	// coord manipulating
	@Override
	public final void setX(final int x) {
		if (x >= 0) {
			super.setX(0);
			return;
		}
		
		if (x <= getParent().getWidth() - getWidth()) {
			super.setX(getParent().getWidth() - getWidth());
			return;
		}
		
		super.setX(x);
	}
	
	@Override
	public final void setY(final int y) {
		if (y >= 0) {
			super.setY(0);
			return;
		}
		
		if (y <= getParent().getHeight() - getHeight()) {
			super.setY(getParent().getHeight() - getHeight());
			return;
		}
		
		super.setY(y);
	}
	
	// listeners
	@Override
	protected final void registerListeners() {
		super.registerListeners();
		Events.addListener(MouseEvent.class, mouseEventListener);
		Events.addListener(SyncEvent.class, syncEventListener);
		Events.addListener(KeyEvent.class, keyEventListener);
	};
	
	@Override
	protected final void unregisterListeners() {
		super.unregisterListeners();
		Events.removeListener(MouseEvent.class, mouseEventListener);
		Events.removeListener(SyncEvent.class, syncEventListener);
		Events.removeListener(KeyEvent.class, keyEventListener);
	};

	// Mouse funcs
	@Override
	public final void mouseClick(final MouseEvent event) { }

	@Override
	public final void mousePressed(final MouseEvent event) { }

	@Override
	public final void mouseReleased(final MouseEvent event) { }

	@Override
	public final void mouseMoved(final MouseEvent event) {
		if ((event.getX() <= MOUSE_DETECT_X_OFFSET) && (event.getX() >= 0)) {
			dx = 1;
		} else if ((event.getX() >= getParent().getWidth() - MOUSE_DETECT_X_OFFSET) && (event.getX() < getParent().getWidth())) {
			dx = -1;
		} else {
			dx = 0;
		}

		if ((event.getY() <= MOUSE_DETECT_Y_OFFSET) && (event.getY() >= 0)) {
			dy = 1;
		} else if ((event.getY() >= getParent().getHeight() - MOUSE_DETECT_Y_OFFSET) && (event.getY() < getParent().getHeight())) {
			dy = -1;
		} else {
			dy = 0;
		}
	}
	
	@Override
	public final void mouseExited(final MouseEvent event) {
		dx = 0;
		dy = 0;
	}
	
	// sync
	@Override
	public final void sync() {
		if (speed++ >= MAP_SPEED) {
			speed = 0;
			if (dx != 0) {
				setX(getX() + dx);
			}
			
			if (dy != 0) {
				setY(getY() + dy);
			}
		}
	}

	@Override
	public final void keyTyped(final KeyEvent event) { }
	
	@Override
	public final void keyPressed(final KeyEvent event) { 
		switch (event.getCode()) {
		case 119:
			setY(getY() + KEY_SPEED);
			break;
		case 115:
			setY(getY() - KEY_SPEED);
			break;
		case 97:
			setX(getX() + KEY_SPEED);
			break;
		case 100:
			setX(getX() - KEY_SPEED);
			break;
		}
	}

	@Override
	public final void keyReleased(final KeyEvent event) { }
}
