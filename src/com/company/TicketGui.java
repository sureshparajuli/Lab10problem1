package com.company;

import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by sparajul on 11/30/15.
 */
public class TicketGui extends JFrame
{
    // Creating initial JObjects
    private final int WIDTH = 180;
    private final int HEIGHT = 150;
    private JPanel optionPanel;
    private JPanel addPanel;
    private JFrame closeFrame;
    private JFrame addFrame;
    private JRadioButton addRadioButton;
    private JRadioButton viewRadioButton;
    private JRadioButton closeRadioButton;
    private ButtonGroup group;
    private JButton exitButton;
    private JButton submitNewButton;
    private JButton submitCloseButton;
    private JTextField descriptionText;
    private JTextField priorityText;
    private JTextField reporterText;
    private JTextField resolutionText;
    private JTextField ticketIDText;
    private JTextArea textArea;
    private static LinkedList<Ticket> ticketQueue = new LinkedList<>();
    private static LinkedList<Ticket> resolvedTickets = new LinkedList<>();

    //main method to run the application for the user
    public static void main(String[] args)
    {
        try
        {
            File saveOpen = new File("open_tickets.txt"); // it makes file object named open_ticket.txt

            Scanner sc = new Scanner(saveOpen);
            // creating variables to hold the datas stored in each line of the text file
            String description;
            int priority;
            String reporter;
            Date date;
            int ticketID;

            // continue to end the file
            while(sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] array = new String[5];
                array = line.split(",");// Comma splits the array into 5

                description = array[0];
                priority = Integer.parseInt(array[1]);
                reporter = array[2];
                date = new Date(array[3]);
                ticketID = Integer.parseInt(array[4]);

                Ticket old = new Ticket(description, priority, reporter, date, ticketID);
                ticketQueue.add(old);
            }
            sc.close(); // closed the open file
            // last ticket from the Queue
            Ticket last = ticketQueue.getLast();
            // it sets the Ticket ID counter to one more  than the last current ticket or open ticket
            Ticket.setStaticTicketIDCounter(last.getTicketID() + 1);

        }

        catch(Exception e)
        {

        }

        new TicketGui(); //call TicketGui constructor to start program
    }

    //GUI constructor
    public TicketGui()
    {
        //display title
        setTitle("Ticket GUI");

        //set size of the window
        setSize(WIDTH, HEIGHT);

        //specify what happens when the close button is clicked
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //get the content pane
        Container contentPane = getContentPane();

        //set the layout of the pane to a BorderLayout
        contentPane.setLayout(new BorderLayout());

        //call method to build options panel
        optionPanel();

        add(optionPanel);

        //set gui to be visible
        setVisible(true);
    }

    private void optionPanel()
    {
        //initialize the panel object
        optionPanel = new JPanel();
        //set the layout of the panel
        optionPanel.setLayout(new FlowLayout());//
        //set the background of the panel
        optionPanel.setBackground(Color.white);

        // Create a radio button to add a ticket
        addRadioButton = new JRadioButton("Add ticket");
        // Add an action listener to the button.
        addRadioButton.addActionListener(new RadioButtonListener());

        // Create a radio button to view open tickets
        viewRadioButton = new JRadioButton("View open tickets");
        // Add an action listener to the button.
        viewRadioButton.addActionListener(new RadioButtonListener());

        // Create a radio button to close an open ticket
        closeRadioButton = new JRadioButton("Close ticket");
        // Add an action listener to the button.
        closeRadioButton.addActionListener(new RadioButtonListener());

        // Add the radio buttons to a group
        group = new ButtonGroup();
        group.add(addRadioButton);
        group.add(viewRadioButton);
        group.add(closeRadioButton);

        // Create a button to exit the program
        exitButton = new JButton("Exit");
        // Add an action listener to the button.
        exitButton.addActionListener(new ButtonListener());

        optionPanel.add(addRadioButton);
        optionPanel.add(viewRadioButton);
        optionPanel.add(closeRadioButton);
        optionPanel.add(exitButton);
    }

    private void addPanel()
    {
        addFrame = new JFrame("View Open Tickets");

        addFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        addFrame.setSize(WIDTH, HEIGHT + 20);

        //initialize the panel object
        addPanel = new JPanel();
        //set the layout of the panel
        addPanel.setLayout(new FlowLayout());
        //set the background of the panel
        addPanel.setBackground(Color.white);

        // Create a text field to hold ticket description
        descriptionText = new JTextField("Desription of the issue?");

        // Create a text field to hold ticket priority
        priorityText = new JTextField("Priority of the issue?");

        // Create a text field to hold ticket reporter
        reporterText = new JTextField("Who is reporting the issue?");

        // Create a button to submit the given info
        submitNewButton = new JButton("Submit New");
        // Add an action listener to the button.
        submitNewButton.addActionListener(new ButtonListener());

        addPanel.add(descriptionText);
        addPanel.add(priorityText);
        addPanel.add(reporterText);
        addPanel.add(submitNewButton);

        addFrame.getContentPane().add(addPanel);

        addFrame.setVisible(true);
    }

    private void viewPanel()
    {
        JFrame frame = new JFrame("Add New Ticket"); //new window for adding ticket

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        frame.setSize(WIDTH + 20, HEIGHT + 100); //same size but 20px extra

        textArea = new JTextArea();
        String toSave = "";
        if(ticketQueue.size() != 0)
        {
            for (Ticket currentTicket : ticketQueue ) {
                toSave += (currentTicket.getDescription() + ", " +
                        currentTicket.getPriority() + ", " +
                        currentTicket.getReporter() + ", " +
                        currentTicket.getDateReported() + ", " +
                        currentTicket.getTicketID() + "\n");
            }
            textArea.setText(toSave); //print to textArea
        }
        else
        {
            textArea.setText("No open tickets!");
        }

        frame.getContentPane().add(textArea);

        frame.setVisible(true);
    }

    private void closePanel()
    {
        closeFrame = new JFrame("View Open Tickets");

        closeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        closeFrame.setSize(WIDTH - 20, HEIGHT);

        //initialize the panel object
        addPanel = new JPanel();
        //set the layout of the panel
        addPanel.setLayout(new FlowLayout());
        //set the background of the panel
        addPanel.setBackground(Color.white);

        ticketIDText = new JTextField("Ticket ID?");

        // Create a text field to hold ticket reporter
        resolutionText = new JTextField("Issue resolution?");

        // Create a button to submit the given info
        submitCloseButton = new JButton("Submit Resolution");
        // Add an action listener to the button.
        submitCloseButton.addActionListener(new ButtonListener());

        addPanel.add(ticketIDText);
        addPanel.add(resolutionText);
        addPanel.add(submitCloseButton);

        closeFrame.getContentPane().add(addPanel);

        closeFrame.setVisible(true);
    }

    private class RadioButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            String button = e.getActionCommand();
            if(button.startsWith("A"))
            {
                addPanel();
            }
            else if(button.startsWith("V"))
            {
                viewPanel();
            }
            else if(button.startsWith("C"))
            {
                closePanel();
            }
        }
    }

    private class ButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            String button = e.getActionCommand();
            if(button.equals("Submit New"))
            {
                Ticket newTicket;

                String description = descriptionText.getText();
                int priority = Integer.parseInt(priorityText.getText());
                String reporter = reporterText.getText();
                Date date = new Date();

                newTicket = new Ticket(description, priority, reporter, date);

                ticketQueue.add(newTicket);
                addFrame.dispose();
            }
            else if(button.equals("Submit Resolution"))
            {
                int ID = Integer.parseInt(ticketIDText.getText());
                String resolution = resolutionText.getText();

                for(Ticket currentTicket : ticketQueue)
                {
                    if(currentTicket.getTicketID() == ID)
                    {
                        currentTicket.setResolution(resolution);
                        resolvedTickets.add(currentTicket);
                        ticketQueue.remove(currentTicket);
                    }
                }
                closeFrame.dispose();
            }
            else if(button.equals("Exit"))
            {
                // creating date of the moment
                Date date = new Date();
                File saveOpen = new File("open_tickets.txt");
                File saveResolved = new File("resolved_as_of_" + date);
                // File is keeping track of open tickets
                try
                {
                    BufferedWriter openWriter = new BufferedWriter(new FileWriter(saveOpen));
                    for (Ticket t : ticketQueue ) {
                        openWriter.write(t.getDescription() + "," +
                                t.getPriority() + "," +
                                t.getReporter() + "," +
                                t.getDateReported() + "," +
                                t.getTicketID() + "\n"); //Write a toString method in Ticket class
                        //println will try to call toString on its argument
                    }
                    openWriter.close();// closing the open file
                    // Opening new file to keep track of result file tickets
                    BufferedWriter resolvedWriter = new BufferedWriter(new FileWriter(saveResolved));
                    for (Ticket t : resolvedTickets ) {
                        resolvedWriter.write(t.getDescription() + "," +
                                t.getPriority() + "," +
                                t.getReporter() + "," +
                                t.getDateReported() + "," +
                                t.getTicketID() + "," +
                                t.getResolution() + "," +
                                t.getDateResolved() + "\n"); //Write a toString method in Ticket class
                        //println will try to call toString on its argument
                    }
                    resolvedWriter.close(); // closing the file

                }
                // Handiling the erro message and throwing Error
                catch(Exception ex)
                {

                }
                System.exit(0);
            }
        }
    }
}