import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.sound.sampled.*;
import javax.swing.*;

public class owenplayer {
    static String songname, name, dirname, music, Playsong, Songnow, finalMusicname;
    static File dir;
    static boolean clicked = false;
    static AudioInputStream audioInputStream;
    static long position = 0;
    static File[] filesList;
    static JFrame frame;
    static int counter = 0;
    static JTextArea showmusic;

    public static AudioInputStream sound(String song) {
        try {
            audioInputStream = AudioSystem.getAudioInputStream(new File(song).getAbsoluteFile());
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new RuntimeException(e);
        }
        return (audioInputStream);
    }

    static Clip clip;

    static {
        try {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    public static void playSound() {
        try {
            clip.open(audioInputStream);
            if (Playsong.equals(Songnow)) {
                clip.setMicrosecondPosition(position);
            } else {
                clip.setMicrosecondPosition(0);
            }
            clip.start();
            Thread.sleep(1000);
            clicked = true;
            System.out.println("start: " + clicked);
            clip.addLineListener(new LineListener() {

                @Override
                public void update(final LineEvent event) {
                    if (event.getType().equals(LineEvent.Type.STOP)) {
                        clicked = false;
                        System.out.println("end: " + clicked);
                    }
                }
            });
        } catch (Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }

    public static void Showmusic(String musicnow) {
        try {
            System.out.println(Songnow);
        } catch (Exception e) {
            System.out.println("not found");
        }
    }

    public static void prepareGUI() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Select Music/Folder");
        // JLabel songNameLabel = new JLabel();//Creating object of song name label
        // Creating object of JButtons
        JPanel listpanel = new JPanel();
        JScrollPane musiclist = new JScrollPane();
        musiclist.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        musiclist.setPreferredSize(new Dimension(10, 1100));
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        Icon playicon = new ImageIcon("playicon.png");
        Icon nexticon = new ImageIcon("forwardicon.png");
        Icon lasticon = new ImageIcon("backicon.png");
        Icon gificon = new ImageIcon("gifcdplayer.gif");
        JLabel gifmusic = new JLabel(gificon);
        JButton selectButton = new JButton("Select song");
        JButton playButton = new JButton(playicon);
        JButton nextButton = new JButton(nexticon);
        JButton lastButton = new JButton(lasticon);
        // Setting properties of JFrame
        frame = new JFrame();
        frame.setTitle("Music Player");
        frame.getContentPane().setBackground(Color.DARK_GRAY);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(200, 100, 840, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        gifmusic.setBounds(355, 50, 300, 300);
        gifmusic.setBorder(null);
        Container c = frame.getContentPane();
        frame.add(gifmusic);
        c.setBounds(0, 0, 190, 600);
        musiclist.setViewportView(listpanel);
        musiclist.setPreferredSize(new Dimension(190, 600));
        musiclist.setBounds(0, 0, 190, 1000);
        listpanel.setBackground(Color.GRAY);
        listpanel.setBounds(0, 10, 190, 600);
        listpanel.setLayout(null);
        //listpanel.setLayout(new BorderLayout());
        c.add(musiclist);
        frame.setVisible(true);
        musiclist.setVisible(true);
        // Setting properties of select mp3 button
        selectButton.setBounds(55, 20, 100, 30);
        selectButton.setOpaque(true);
        selectButton.setBackground(Color.LIGHT_GRAY);
        selectButton.setBorder(null);
        selectButton.setBorderPainted(false);
        // selectButton.setContentAreaFilled(false);
        listpanel.add(selectButton);
        System.out.println(clicked);
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String musicname = null;
                File songdir;
                Pattern pattern = Pattern.compile("^(?:.*\\/)*([^\\/\\r\\n]+?|)(?=(?:\\.[^\\/\\r\\n.]*)?$)");
                int i = 0;
                fc.showOpenDialog(null);
                try {
                    name = fc.getSelectedFile().toString();
                } catch (Exception NullPointerException) {
                    System.out.println("Window Closed");
                }
                if (name.endsWith(".wav")) {
                    songname = name;
                    songdir = new File(songname);
                    Playsong = songdir.toString();
                    if (clicked == false) {
                        sound(Playsong);
                        playSound();
                        showmusic.setText(null);
                        Showmusic(Playsong);
                    }
                } else {
                    dirname = name;
                    dir = new File(dirname);
                    filesList = dir.listFiles();
                    for (File file : filesList) {
                        String finalPath;
                        String path;
                        music = file.getName();
                        Matcher matcher = pattern.matcher(music);
                        boolean matchFound = matcher.find();
                        if (matchFound) {
                            System.out.println(matcher);
                        } else {
                            System.out.println("Match not found");
                        }
                        try {
                            musicname = matcher.group(0);
                            musicname.strip();
                        } catch (Exception IllegalStateException) {
                            JOptionPane.showMessageDialog(frame, "Please Choose MP3/wav music files!",
                                    "Wrong FIle Format", JOptionPane.WARNING_MESSAGE);
                        }
                        JButton musicbutton = new JButton(musicname);
                        System.out.println(matcher.group(0));
                        path = file.getAbsolutePath();
                        System.out.println("File path: " + path + "\n");
                        //listpanel.setBounds(0, 10, 160, 600 + i);
                        // musiclist.setBounds(0, 0, 190, 1000+i);
                        musicbutton.setBounds(10, 60 + i, 150, 30);
                        musicbutton.setOpaque(true);
                        musicbutton.setBackground(Color.lightGray);
                        musicbutton.setBorder(null);
                        musicbutton.setBorderPainted(false);
                        musicbutton.addMouseListener(new MouseAdapter() {
                            public void mouseEntered(MouseEvent evt) {
                                musicbutton.setOpaque(true);
                                musicbutton.setBackground(Color.PINK);
                                musicbutton.setBorder(null);
                                musicbutton.setBorderPainted(false);
                            }

                            public void mouseExited(MouseEvent evt) {
                                musicbutton.setOpaque(true);
                                musicbutton.setBackground(Color.lightGray);
                                musicbutton.setBorder(null);
                                musicbutton.setBorderPainted(false);
                            }
                        });
                        finalPath = path;
                        finalMusicname = musicname;
                        System.out.println("Final path: " + path + "\n");
                        musicbutton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                int right1 = 0;
                                Playsong = finalPath;
                                System.out.println("Playsong: " + Playsong);
                                for (int q = 0; q < filesList.length; q++) {
                                    if (String.valueOf(filesList[q]).equals(Playsong)) {
                                        Songnow = Playsong;
                                        right1 = q;
                                        System.out.println("Songnow: " + Playsong + " right1: " + right1);
                                    }
                                }
                                sound(Playsong);
                                playSound();
                                showmusic.setText(null);
                                Showmusic(Playsong);
                                System.out.println("Now Playing: " + Playsong);
                                while (clicked == false) {
                                    try {
                                        clip.stop();
                                        clip.close();
                                        Playsong = String.valueOf(filesList[right1 + 1]);
                                    } catch (Exception ArrayIndexOutOfBoundsException) {
                                        clip.stop();
                                        clip.close();
                                        Playsong = String.valueOf(filesList[right1]);
                                    }
                                    sound(Playsong);
                                    playSound();
                                    System.out.println("Change: " + Playsong);
                                }
                            }
                        });
                        listpanel.add(musicbutton);
                        i = i + 40;
                        counter++;
                    }
                }
            }
        });
        // Setting properties of play button
        playButton.setBounds(470, 460, 60, 60);
        playButton.setBorder(null);
        playButton.setBorderPainted(false);
        playButton.setContentAreaFilled(false);
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (clicked == false) {
                    sound(Playsong);
                    playSound();
                    showmusic.setText(null);
                    Showmusic(Playsong);
                } else if (clicked == true) {
                    position = clip.getMicrosecondPosition();
                    Songnow = Playsong;
                    clip.stop();
                    clip.close();
                    System.out.println(position);
                }
            }
        });
        c.add(playButton);

        nextButton.setBounds(570, 460, 60, 60);
        nextButton.setBorder(null);
        nextButton.setBorderPainted(false);
        nextButton.setContentAreaFilled(false);
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int right = 0;
                for (int q = 0; q < filesList.length; q++) {
                    if (String.valueOf(filesList[q]).equals(Playsong)) {
                        Songnow = Playsong;
                        right = q;
                        clip.stop();
                        clip.close();
                    }
                }
                try {
                    Playsong = String.valueOf(filesList[right + 1]);
                } catch (Exception ArrayIndexOutOfBoundsException) {
                    Playsong = String.valueOf(filesList[right]);
                }
                System.out.println("Change: " + Playsong);
                sound(Playsong);
                playSound();
                showmusic.setText(null);
                Showmusic(Playsong);
                System.out.println("Now Playing: " + Playsong);
            }
        });
        c.add(nextButton);

        lastButton.setBounds(370, 460, 60, 60);
        lastButton.setBorder(null);
        lastButton.setBorderPainted(false);
        lastButton.setContentAreaFilled(false);
        lastButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent er) {
                int right1 = 0;
                for (int w = 0; w < filesList.length; w++) {
                    if (String.valueOf(filesList[w]).equals(Playsong)) {
                        Songnow = Playsong;
                        clip.stop();
                        clip.close();
                        right1 = w;
                    }
                }
                try {
                    Playsong = String.valueOf(filesList[right1 - 1]);
                } catch (Exception ArrayIndexOutOfBoundsException) {
                    Playsong = String.valueOf(filesList[right1]);
                }
                System.out.println("Change: " + Playsong);
                sound(Playsong);
                playSound();
                showmusic.setText(null);
                Showmusic(Playsong);
                System.out.println("Now Playing: " + Playsong);
            }
        });
        c.add(lastButton);
        clip.close();
        frame.setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        prepareGUI();
    }
}