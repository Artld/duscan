package gui;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.*;

class DuFrame extends JFrame
{
    private static final long serialVersionUID = 1L;

    public DuFrame()
    {
        setTitle("Duscan - Duplicate scanner");
        setBounds(150, 100, 800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final DuPanel panel = new DuPanel();
        add(panel);

        // Resize event detection
        addComponentListener(new ComponentListener() 
        {
            @Override
            public void componentResized(ComponentEvent e)
            {
                panel.resizePanelComponents(getBounds());
            }
            @Override
            public void componentMoved(ComponentEvent e){}
            @Override
            public void componentShown(ComponentEvent e){}
            @Override
            public void componentHidden(ComponentEvent e){}
        });

        setLayout(null);
        setVisible(true);
    }
}