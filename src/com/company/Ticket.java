package com.company;

import java.util.Date;
import java.util.Scanner;
import java.util.LinkedList;

public class Ticket {

    private int priority;
    private String reporter; //Stores person or department who reported issue
    private String description;
    private Date dateReported;
    // adding variables for Problems 5.
    private Date dateResolved;
    private String resolution;

    //STATIC Counter - accessible to all Ticket objects.
    //If any Ticket object modifies this counter, all Ticket objects will have the modified value
    //Make it private - only Ticket objects should have access
    private static int staticTicketIDCounter = 1;
    //The ID for each ticket - instance variable. Each Ticket will have it's own ticketID variable
    protected int ticketID;

    public Ticket(String desc, int p, String rep, Date date) {
        this.description = desc;
        this.priority = p;
        this.reporter = rep;
        this.dateReported = date;
        this.ticketID = staticTicketIDCounter;
        staticTicketIDCounter++;
    }

    public Ticket(String desc, int p, String rep, Date date, int id) {
        this.description = desc;
        this.priority = p;
        this.reporter = rep;
        this.dateReported = date;
        this.ticketID = id;
    }


    protected int getPriority() {
        return priority;
    }

    //Added method to retrieve ticketID
    protected int getTicketID() {
        return ticketID;
    }

    //Added method to retrieve ticket description
    protected String getDescription() {
        return description;
    }

    protected String getReporter() { return reporter; }

    protected Date getDateReported() { return dateReported; }

    protected String getResolution()
    {
        return resolution;
    }

    protected Date getDateResolved() {
        return dateResolved;
    }

    protected void setResolution(String resolution)
    {
        Date dateReported = new Date();
        this.resolution = resolution;
        this.dateResolved = dateReported;
    }

    protected static void setStaticTicketIDCounter(int num)
    {
        staticTicketIDCounter = num;
    }

    public String toString(){
        return("ID= " + this.ticketID + " Issued: " + this.description + " Priority: " + 					this.priority + " Reported by: "
                + this.reporter + " Reported on: " + this.dateReported);
    }

    protected static void deleteTicket(LinkedList<Ticket> ticketQueue) {
        //Added TICKETMANAGER class call to make static method work
        //TicketManager.printAllTickets(ticketQueue);   //display list for user

        if (ticketQueue.size() == 0) {    //no tickets!
            System.out.println("No tickets to delete!\n");
            return;
        }

        Scanner deleteScanner = new Scanner(System.in);
        System.out.println("Enter ID of ticket to delete");
        int deleteID = deleteScanner.nextInt();

        //Loop over all tickets. Delete the one with this ticket ID
        boolean found = false;
        for (Ticket ticket : ticketQueue) {
            if (ticket.getTicketID() == deleteID) {
                found = true;
                ticketQueue.remove(ticket);
                System.out.println(String.format("Ticket %d deleted", deleteID));
                break; //don't need loop any more.
            }
        }
        if (found == false) {
            System.out.println("Ticket ID not found, no ticket deleted");
            //TODO – re-write this method to ask for ID again if not found
        }

        //Added TICKETMANAGER class call to make static method work
        //TicketGui.printAllTickets(ticketQueue);  //print updated list

    }

    protected static void addTickets(LinkedList<Ticket> ticketQueue) {
        Scanner sc = new Scanner(System.in);
        boolean moreProblems = true;
        String description, reporter;
        Date dateReported = new Date(); //Default constructor creates date with current date/time
        int priority;

        while (moreProblems){
            System.out.println("Enter problem");
            description = sc.nextLine();
            System.out.println("Who reported this issue?");
            reporter = sc.nextLine();
            System.out.println("Enter priority of " + description);
            priority = Integer.parseInt(sc.nextLine());

            Ticket t = new Ticket(description, priority, reporter, dateReported);
            //ticketQueue.add(t);
            addTicketInPriorityOrder(ticketQueue, t);

            //Added TICKETMANAGER class call to make static method work
            //TicketGui.printAllTickets(ticketQueue);

            System.out.println("More tickets to add?");
            String more = sc.nextLine();
            if (more.equalsIgnoreCase("N")) {
                moreProblems = false;
            }
        }
    }

    protected static void addTicketInPriorityOrder(LinkedList<Ticket> tickets, Ticket newTicket){

        //Logic: assume the list is either empty or sorted

        if (tickets.size() == 0 ) {//Special case - if list is empty, add ticket and return
            tickets.add(newTicket);
            return;
        }

        //Tickets with the HIGHEST priority number go at the front of the list. (e.g. 5=server on fire)
        //Tickets with the LOWEST value of their priority number (so the lowest priority) go at the end

        int newTicketPriority = newTicket.getPriority();

        for (int x = 0; x < tickets.size() ; x++) {    //use a regular for loop so we know which element we are looking at

            //if newTicket is higher or equal priority than the this element, add it in front of this one, and return
            if (newTicketPriority >= tickets.get(x).getPriority()) {
                tickets.add(x, newTicket);
                return;
            }
        }

        //Will only get here if the ticket is not added in the loop
        //If that happens, it must be lower priority than all other tickets. So, add to the end.
        tickets.addLast(newTicket);
    }
}