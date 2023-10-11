import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

class EditorDropTarget implements DropTargetListener, PropertyChangeListener {
    private JEditorPane pane;
    private DropTarget dropTarget;
    private boolean acceptableType;
    private boolean draggingFile;
    private Color backgroundColor;
    private boolean changingBackground;
    private static final Color feedbackColor = new Color(241, 243, 243);

    public EditorDropTarget(JEditorPane pane) {
        this.pane = pane;
        pane.addPropertyChangeListener(this);
        backgroundColor = pane.getBackground();
        dropTarget = new DropTarget(pane, DnDConstants.ACTION_COPY_OR_MOVE, this, pane.isEnabled(), null);
    }

    public void dragEnter(DropTargetDragEvent dtde) {
        DnDUtils.debugPrintln("dragEnter, drop action = " + DnDUtils.showActions(dtde.getDropAction()));
        checkTransferType(dtde);
        boolean acceptedDrag = acceptOrRejectDrag(dtde);
        dragUnderFeedback(dtde, acceptedDrag);
    }

    public void dragExit(DropTargetEvent dte) {
        DnDUtils.debugPrintln("DropTarget dragExit");
        dragUnderFeedback(null, false);
    }

    public void dragOver(DropTargetDragEvent dtde) {
        DnDUtils.debugPrintln("DropTarget dragOver, drop action = " + DnDUtils.showActions(dtde.getDropAction()));
        boolean acceptedDrag = acceptOrRejectDrag(dtde);
        dragUnderFeedback(dtde, acceptedDrag);
    }

    public void dropActionChanged(DropTargetDragEvent dtde) {
        DnDUtils.debugPrintln("DropTarget dropActionChanged, drop action = "
                + DnDUtils.showActions(dtde.getDropAction()));
        boolean acceptedDrag = acceptOrRejectDrag(dtde);
        dragUnderFeedback(dtde, acceptedDrag);
    }

    public void drop(DropTargetDropEvent dtde) {
        DnDUtils.debugPrintln("DropTarget drop, drop action = "
                + DnDUtils.showActions(dtde.getDropAction()));
        if ((dtde.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) != 0) {
            dtde.acceptDrop(dtde.getDropAction());
            Transferable transferable = dtde.getTransferable();
            try {
                boolean result = false;
                if (draggingFile) {
                    result = dropFile(transferable);
                } else {
                    result = dropContent(transferable, dtde);
                }
                dtde.dropComplete(result);
                DnDUtils.debugPrintln("Drop completed, success: " + result);
            } catch (Exception e) {
                DnDUtils.debugPrintln("Exception while handling drop " + e);
                dtde.rejectDrop();
            }
        } else {
            DnDUtils.debugPrintln("Drop target rejected drop");
            dtde.dropComplete(false);
        }
    }

    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        if (propertyName.equals("enabled")) {
            dropTarget.setActive(pane.isEnabled());
        } else if (!changingBackground && propertyName.equals("background")) {
            backgroundColor = pane.getBackground();
        }
    }

    protected boolean acceptOrRejectDrag(DropTargetDragEvent dtde) {
        int dropAction = dtde.getDropAction();
        int sourceActions = dtde.getSourceActions();
        boolean acceptedDrag = false;
        DnDUtils.debugPrintln("\tSource actions are "
                + DnDUtils.showActions(sourceActions) + ", drop action is "
                + DnDUtils.showActions(dropAction));
        if (!acceptableType
                || (sourceActions & DnDConstants.ACTION_COPY_OR_MOVE) == 0) {
            DnDUtils.debugPrintln("Drop target rejecting drag");
            dtde.rejectDrag();
        } else if (!draggingFile && !pane.isEditable()) {
            DnDUtils.debugPrintln("Drop target rejecting drag");
            dtde.rejectDrag();
        } else if ((dropAction & DnDConstants.ACTION_COPY_OR_MOVE) == 0) {
            DnDUtils.debugPrintln("Drop target offering COPY");
            dtde.acceptDrag(DnDConstants.ACTION_COPY);
            acceptedDrag = true;
        } else {
            DnDUtils.debugPrintln("Drop target accepting drag");
            dtde.acceptDrag(dropAction);
            acceptedDrag = true;
        }
        return acceptedDrag;
    }

    protected void dragUnderFeedback(DropTargetDragEvent dtde, boolean acceptedDrag) {
        if (draggingFile) {
            Color newColor = (dtde != null && acceptedDrag ? feedbackColor
                    : backgroundColor);
            if (newColor.equals(pane.getBackground()) == false) {
                changingBackground = true;
                pane.setBackground(newColor);
                changingBackground = false;
                pane.repaint();
            }
        } else {
            if (dtde != null && acceptedDrag) {
                Point location = dtde.getLocation();
                pane.getCaret().setVisible(true);
                pane.setCaretPosition(pane.viewToModel(location));
            } else {
                pane.getCaret().setVisible(false);
            }
        }
    }

    protected void checkTransferType(DropTargetDragEvent dtde) {
        acceptableType = false;
        draggingFile = false;
        if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            acceptableType = true;
            draggingFile = true;
        } else if (dtde.isDataFlavorSupported(DataFlavor.plainTextFlavor)
                || dtde.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            acceptableType = true;
        }
        DnDUtils.debugPrintln("File type acceptable - " + acceptableType);
        DnDUtils.debugPrintln("Dragging a file - " + draggingFile);
    }

    protected boolean dropFile(Transferable transferable) throws IOException,
            UnsupportedFlavorException, MalformedURLException {
        java.util.List fileList = (List) transferable.getTransferData(DataFlavor.javaFileListFlavor);
        File transferFile = (File) fileList.get(0);
        final URL transferURL = transferFile.toURL();
        DnDUtils.debugPrintln("File URL is " + transferURL);
        pane.setPage(transferURL);
        return true;
    }

    protected boolean dropContent(Transferable transferable, DropTargetDropEvent dtde) {
        if (!pane.isEditable()) {
            return false;
        }
        try {
            DataFlavor[] flavors = dtde.getCurrentDataFlavors();
            DataFlavor selectedFlavor = null;
            for (int i = 0; i < flavors.length; i++) {
                DataFlavor flavor = flavors[i];
                DnDUtils.debugPrintln("Drop MIME type " + flavor.getMimeType()
                        + " is available");
                if (flavor.equals(DataFlavor.plainTextFlavor)
                        || flavor.equals(DataFlavor.stringFlavor)) {
                    selectedFlavor = flavor;
                    break;
                }
            }
            if (selectedFlavor == null) {
                return false;
            }
            DnDUtils.debugPrintln("Selected flavor is "
                    + selectedFlavor.getHumanPresentableName());
            Object data = transferable.getTransferData(selectedFlavor);
            DnDUtils.debugPrintln("Transfer data type is "
                    + data.getClass().getName());
            String insertData = null;
            if (data instanceof InputStream) {
                String charSet = selectedFlavor.getParameter("charset");
                InputStream is = (InputStream) data;
                byte[] bytes = new byte[is.available()];
                is.read(bytes);
                try {
                    insertData = new String(bytes, charSet);
                } catch (UnsupportedEncodingException e) {
                    insertData = new String(bytes);
                }
            } else if (data instanceof String) {
                insertData = (String) data;
            }
            if (insertData != null) {
                int selectionStart = pane.getCaretPosition();
                pane.replaceSelection(insertData);
                pane.select(selectionStart, selectionStart
                        + insertData.length());
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}

class DnDUtils {
    public static String showActions(int action) {
        String actions = "";
        if ((action & (DnDConstants.ACTION_LINK | DnDConstants.ACTION_COPY_OR_MOVE)) == 0) {
            return "None";
        }
        if ((action & DnDConstants.ACTION_COPY) != 0) {
            actions += "Copy ";
        }
        if ((action & DnDConstants.ACTION_MOVE) != 0) {
            actions += "Move ";
        }
        if ((action & DnDConstants.ACTION_LINK) != 0) {
            actions += "Link";
        }
        return actions;
    }

    public static boolean isDebugEnabled() {
        return debugEnabled;
    }

    public static void debugPrintln(String s) {
        if (debugEnabled) {
            System.out.println(s);
        }
    }

    private static boolean debugEnabled = (System.getProperty("DnDExamples.debug") != null);
}

class AutoScrollingEditorPane extends JEditorPane implements Autoscroll {
    public static final Insets defaultScrollInsets = new Insets(8, 8, 8, 8);

    protected Insets scrollInsets = defaultScrollInsets;

    public AutoScrollingEditorPane() {
    }

    public void setScrollInsets(Insets insets) {
        this.scrollInsets = insets;
    }

    public Insets getScrollInsets() {
        return scrollInsets;
    }

    public Insets getAutoscrollInsets() {
        Rectangle r = getVisibleRect();
        Dimension size = getSize();
        Insets i = new Insets(r.y + scrollInsets.top, r.x + scrollInsets.left,
                size.height - r.y - r.height + scrollInsets.bottom, size.width
                - r.x - r.width + scrollInsets.right);
        return i;
    }

    public void autoscroll(Point location) {
        JScrollPane scroller = (JScrollPane) SwingUtilities.getAncestorOfClass(
                JScrollPane.class, this);
        if (scroller != null) {
            JScrollBar hBar = scroller.getHorizontalScrollBar();
            JScrollBar vBar = scroller.getVerticalScrollBar();
            Rectangle r = getVisibleRect();
            if (location.x <= r.x + scrollInsets.left) {
                hBar.setValue(hBar.getValue() - hBar.getUnitIncrement(-1));
            }
            if (location.y <= r.y + scrollInsets.top) {
                vBar.setValue(vBar.getValue() - vBar.getUnitIncrement(-1));
            }
            if (location.x >= r.x + r.width - scrollInsets.right) {
                hBar.setValue(hBar.getValue() + hBar.getUnitIncrement(1));
            }
            if (location.y >= r.y + r.height - scrollInsets.bottom) {
                vBar.setValue(vBar.getValue() + vBar.getUnitIncrement(1));
            }
        }
    }
}