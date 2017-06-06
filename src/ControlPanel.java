import javax.sound.midi.Instrument;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by brian on 5/29/2017.
 */

public class ControlPanel extends JPanel {

    private int currentInstrument;
    private int tempo;         //in BPM
    private boolean timeSignature4;  //beats per measure and each beat is a quarter note
    private volatile boolean isPlaying = false;
    private boolean isChanged = false;
    private boolean[][] beatArray = new boolean[DrumSounds.NUM_SOUNDS][16];

    private JLabel instrumentLabel = new JLabel();
    private JLabel tempoLabel = new JLabel();
    private JSlider tempoSlider;
    private JSlider instrumentSlider;
    private JToggleButton startButton;
    private RhythmPanel player;


    public ControlPanel(RhythmPanel player) {
        this.player = player;
        tempo = 120;
        timeSignature4 = true;

        setLayout(new GridLayout(5, 2));

        tempoSlider = new JSlider(40, 300, tempo);
        tempoSlider.setMajorTickSpacing(5);
        tempoSlider.setPaintTicks(true);
        tempoSlider.setSnapToTicks(true);
        tempoSlider.addChangeListener(new TempoChangeListener());

        add(tempoSlider);
        tempoLabel.setText("Tempo: " + tempoSlider.getValue());
        add(tempoLabel);

        instrumentSlider = new JSlider(0, DrumSounds.NUM_SOUNDS - 1, 0);
        instrumentSlider.setMajorTickSpacing(1);
        instrumentSlider.setPaintTicks(true);
        instrumentSlider.setSnapToTicks(true);
        instrumentSlider.addChangeListener(new InstrumentChangeListener());

        add(instrumentSlider);
        add(instrumentLabel);
        instrumentLabel.setText("Instrument: " + DrumSounds.audioNames[instrumentSlider.getValue()]);

        JRadioButton time4 = new JRadioButton("4", true);
        JRadioButton time3 = new JRadioButton("3");
        time4.setActionCommand("4");
        time3.setActionCommand("3");

        time4.addActionListener(new TimeActionListener());
        time3.addActionListener(new TimeActionListener());

        ButtonGroup time = new ButtonGroup();
        time.add(time4);
        time.add(time3);

        JPanel timePanel = new JPanel(new GridLayout(2, 1));
        timePanel.add(time3);
        timePanel.add(time4);

        add(timePanel);
        add(new JLabel("Time Signature"));


        startButton = new JToggleButton("Start", false);
        startButton.setActionCommand("start");

        startButton.addActionListener(new StartActionListener());

        startButton.setPreferredSize(new Dimension(10, 40));

        add(startButton);
        add(new JLabel());

        JButton resetButton = new JButton(("Reset"));
        resetButton.addActionListener(new ResetChangeListener());
        resetButton.setPreferredSize(new Dimension(10, 40));

        add(resetButton);
    }

    public int getCurrentInstrument() {
        return currentInstrument;
    }

    public int getTempo() {
        return tempo;
    }

    public boolean getTimeSignature() {
        return timeSignature4;
    }

    public boolean[][] getBeatArray() {
        return beatArray;
    }

    public boolean isChanged() {
        return isChanged;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setChangedFalse() {
        isChanged = false;
    }


    public class TempoChangeListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            JSlider root = (JSlider) e.getSource();
            if (!root.getValueIsAdjusting()) {
                tempo = (int) root.getValue();
                tempoLabel.setText("Tempo: " + root.getValue());

            }
        }
    }

    public class TimeActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            timeSignature4 = !timeSignature4;

        }
    }

    public class StartActionListener implements ActionListener {

        @Override
        public synchronized void actionPerformed(ActionEvent e) {
            isPlaying = !isPlaying;
            System.out.println(isPlaying);
        }
    }

    public class InstrumentChangeListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            JSlider root = (JSlider) e.getSource();
            if (!root.getValueIsAdjusting()) {
                player.updateBeats(beatArray[currentInstrument]);
                currentInstrument = root.getValue();
                player.setInstrument(currentInstrument, beatArray[currentInstrument]);
                player.updateBeats(beatArray[currentInstrument]);
                instrumentLabel.setText("Instrument: " + DrumSounds.audioNames[root.getValue()]);
            }
        }
    }

    public class ResetChangeListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            for (int y = 0; y < beatArray.length; y++) {
                for (boolean b : beatArray[y])
                    b = false;
            }
            instrumentSlider.setValue(0);
            tempoSlider.setValue(120);
            isPlaying = false;
            startButton.setSelected(false);
            player.clearBeats();

        }
    }
}