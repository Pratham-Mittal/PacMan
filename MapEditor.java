import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ThreadLocalRandom;

public class MapEditor extends JFrame {

    public MapEditor(){
        setSize(650,400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        getContentPane().setBackground(Color.black);

        JPanel sideBar = new JPanel();
        sideBar.setLayout(new BorderLayout());
        sideBar.setBackground(Color.black);
        JPanel VirusSelection = new JPanel();
        VirusSelection.setLayout(new BoxLayout(VirusSelection,BoxLayout.Y_AXIS));
        VirusSelection.setBackground(Color.black);
        JLabel l0 = new JLabel("= : Blank Space (without Food)");
        JLabel l1 = new JLabel("_ : Blank Space (with Food)");
        JLabel l2 = new JLabel("X : Wall");
        JLabel l3 = new JLabel("Y : Semi-Wall (Passable by virus)");
        JLabel l4 = new JLabel("P : man Start Position");
        JLabel l5 = new JLabel("1 : Red virus (Chaser)");
        JLabel l6 = new JLabel("2 : Pink virus (Traveler)");
        JLabel l7 = new JLabel("3 : Blue Virus (Patrol)");
        JLabel l8 = new JLabel("F : Fruit");
        JLabel l9 = new JLabel("B : Virus Base");

        l0.setForeground(Color.yellow);
        l1.setForeground(Color.yellow);
        l2.setForeground(Color.yellow);
        l3.setForeground(Color.yellow);
        l4.setForeground(Color.yellow);
        l5.setForeground(Color.yellow);
        l6.setForeground(Color.yellow);
        l7.setForeground(Color.yellow);
        l8.setForeground(Color.yellow);
        l9.setForeground(Color.yellow);

        VirusSelection.add(l0);
        VirusSelection.add(l1);
        VirusSelection.add(l2);
        VirusSelection.add(l3);
        VirusSelection.add(l4);
        VirusSelection.add(l5);
        VirusSelection.add(l6);
        VirusSelection.add(l7);
        VirusSelection.add(l8);
        VirusSelection.add(l9);

        setLayout(new BorderLayout());
        sideBar.add(VirusSelection,BorderLayout.NORTH);
        getContentPane().add(sideBar,BorderLayout.EAST);

        JTextArea ta = new JTextArea();
        ta.setBackground(Color.black);
        ta.setForeground(Color.yellow);
        ta.setText("XXXXXXXXXX\n"
                +  "XP_______X\n"
                +  "X________X\n"
                +  "X________X\n"
                +  "XXXXXXXXXX");
        ta.setBorder(new CompoundBorder(new CompoundBorder(new EmptyBorder(20,10,20,10),new LineBorder(Color.yellow)),new EmptyBorder(10,10,10,10)));
        getContentPane().add(ta);


        Button startButton = new Button("Start Game");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GameWindow(compileMap(ta.getText()));
            }
        });
        sideBar.add(startButton,BorderLayout.SOUTH);

        setVisible(true);
    }

    //Resolve Map
    public static MapData compileMap(String input){
        int mx = input.indexOf('\n');
        int my = StringHelper.countLines(input);
        System.out.println("Making Map "+mx+"x"+my);

        MapData customMap = new MapData(mx,my);
        customMap.setCustom(true);
        int[][] map = new int[mx][my];

        //Pass Map As Argument
        int i=0;
        int j=0;
        for(char c : input.toCharArray()){
            if(c == '1'){
                map[i][j] = 0;
                customMap.getVirussData().add(new Data(i,j, VirusType.RED));
            }
            if(c == '2'){
                map[i][j] = 0;
                customMap.getVirussData().add(new Data(i,j, VirusType.PINK));
            }
            if(c == '3'){
                map[i][j] = 0;
                customMap.getVirussData().add(new Data(i,j, VirusType.CYAN));
            }
            if(c == 'P'){
                map[i][j] = 0;
                customMap.setCovPosition(new Point(i,j));
            }
            if(c == 'X'){
                map[i][j] = 23;
            }
            if(c == 'Y'){
                map[i][j] = 26;
            }
            if(c == '_'){
                map[i][j] = 0;
                customMap.getFoodPositions().add(new Food(i,j));
            }
            if(c == '='){
                map[i][j] = 0;
            }
            if(c == 'O'){
                map[i][j] = 0;
                customMap.getPufoodPositions().add(new PowerUp(i,j,0));
            }
            if(c == 'F'){
                map[i][j] = 0;
                customMap.getPufoodPositions().add(new PowerUp(i,j, ThreadLocalRandom.current().nextInt(4)+1));
            }
            if(c == 'B'){
                map[i][j] = 0;
                customMap.setGVirusBasePosition(new Point(i,j));
            }
            i++;
            if(c == '\n'){
                j++;
                i=0;
            }
        }



        customMap.setMap(map);
        customMap.setCustom(true);
        System.out.println("Map Read OK !");
        return customMap;

    }

}
