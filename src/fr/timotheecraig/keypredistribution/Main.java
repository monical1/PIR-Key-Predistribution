package fr.timotheecraig.keypredistribution;

import fr.timotheecraig.keypredistribution.enums.NetworkType;
import fr.timotheecraig.keypredistribution.external.Attacker;
import fr.timotheecraig.keypredistribution.main.Network;
import fr.timotheecraig.keypredistribution.ui.Display;

import java.awt.*;
import java.io.PrintWriter;

/**
 * Created by Timothee on 11/04/2017.
 */
public class Main {

    public static void main(String[] args) {

        // Travailler avec unsigned ints et la plus grande structure java

        int keysPerNode = 25;
        int amountOfKeys = 1000;
        int sizeOfKey = 128;
        int amountOfNodesToCompromise = 0;
        int polynomialsOrder = 66;
        int degree = 6; // 4 nodes
        int size = 1000; // 1000 meters
        int nodeEmissionRadius = 50; // 50 meters

        // BASIC SCHEME

        try {

            PrintWriter pr = new PrintWriter("data-collected.txt", "UTF-8");



            System.out.println("Key Predistribution Simulation - By Timothée Craig");
            System.out.println("--------------------------------------------------");
            System.out.println("");

            Network network = Network.getByDegree(degree, size, nodeEmissionRadius, NetworkType.basicScheme);

            System.out.println("        Done with network initialisation          ");
            System.out.println("");
            System.out.println("                Adding keys...                    ");
            System.out.println("--------------------------------------------------");
            System.out.println("");

            network.addAmountOfKeys(amountOfKeys, sizeOfKey);
            System.out.println(network);

            System.out.println("");
            System.out.println("              Pre-Distributing keys               ");
            System.out.println("--------------------------------------------------");
            System.out.println("");

            network.predistributeKeys(keysPerNode); // 5 keys per node
            network.setNodesInitialised();

            //network.getNodes().forEach(System.out::println);

            // network.displayKeys();
            System.out.println("             Pre-distribution Completed           ");
            System.out.println("");
            System.out.println("                     Deploying...                 ");
            System.out.println("         Initializing neighbour discovery         ");
            System.out.println("--------------------------------------------------");
            System.out.println("");

            network.neighbourDiscovery();
            //network.getNodes().forEach(System.out::println);

            System.out.println("            Neighbour discovery completed         ");
            System.out.println("");
            System.out.println("                  Creating paths                  ");
            System.out.println("--------------------------------------------------");
            System.out.println("");

            network.createLinks_basic();

            double ratio = ((double) (network.getTotalNumberOfSecuredLinks())) / network.getTotalNumberOfLinks();
            System.out.println("Amount Of Secure Links / Amount of Links = " + ratio);

            int amountOfLinks = network.getLinks() != null ? network.getLinks().size() : 0;
            System.out.println("Amount of links created: " + amountOfLinks);

            int maxAmountOfNodes = network.getNodes().size();
            int t = 1;
            //while(t < maxAmountOfNodes) {
            System.out.println("");
            System.out.println("           Attacker attacks the network           ");
            System.out.println("--------------------------------------------------");
            System.out.println("");

//            int amountOfCompromisedLinks = Attacker.compromiseNetwork_Basic_Scheme(t, network);

//            System.out.println("Resilience for "+ t +" nodes compromised, with "
//                    + keysPerNode + " keys each : " + ((double) amountOfCompromisedLinks / amountOfLinks));

            // The generated graph will look bad here because of the randomness in choosing node to corrupt
            // Potentially we could make it less random, in order to have a nicer graph. (giving the attacker the indexes
            // of the nodes to corrupt.

            Display d = new Display(network, size, degree, keysPerNode, amountOfNodesToCompromise, amountOfKeys, polynomialsOrder, amountOfNodesToCompromise);
            Graphics g = d.getGraphics();

//            pr.println(t+";"+((double)amountOfCompromisedLinks/amountOfLinks));
            //Attacker.uncompromiseNodes(network);
//                t++;

//                Thread.sleep(500);

            //}

            pr.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        //
        //
        //


        // Polynomial pool-based key pre-distribution for Wireless Sensor Networks (after basic scheme)
/*
        try {

            //int m = 5; // Size of subset

            PrintWriter pr = new PrintWriter("data-collected.txt", "UTF-8");

            //while (m <= 70) {

            System.out.println("Key Predistribution Simulation - By Timothée Craig");
            System.out.println("--------------------------------------------------");

            Network network = Network.getByDegree(degree, size, nodeEmissionRadius, NetworkType.polynomialScheme);
            System.out.println(network);

            System.out.println();
            System.out.println("        Done with network initialisation          ");
            System.out.println("             Adding polynomials...                ");
            System.out.println("--------------------------------------------------");
            System.out.println();

            network.generatePolynomialPool(amountOfKeys, polynomialsOrder, (int) Math.pow(2, 8));
            System.out.println(network);

//            network.displayPolynomialPool();

//        for (Node node : network.getNodes()) {
//            System.out.println(node);
//        }

            System.out.println();
            System.out.println("           Pre-Distributing polynomials           ");
            System.out.println("--------------------------------------------------");
            System.out.println();

            network.predistributePolynomials(keysPerNode);
            network.setNodesInitialised();

            //network.getNodes().forEach(Node::displayPolynomials);
            // network.displayKeys(); // Keys are random atm, better make it less random sometime

            System.out.println();
            System.out.println("                     Deploying...                 ");
            System.out.println("         Initializing neighbour discovery         ");
            System.out.println("--------------------------------------------------");
            System.out.println();

            network.neighbourDiscovery();

            System.out.println();
            System.out.println("                    Creating paths                ");
            System.out.println("--------------------------------------------------");
            System.out.println();

            network.createLinks_polynomials();
            double ratio = ((double) (network.getTotalNumberOfSecuredLinks())) / network.getTotalNumberOfLinks();
            System.out.println("Amount Of Secure Links / Amount of Links = " + ratio);

            int amountOfLinks = network.getLinks() != null ? network.getLinks().size() : 0;
            System.out.println("Amount of links created: " + amountOfLinks + ", Total amount of links : " + network.getTotalNumberOfLinks());

//        pr.println(ratio);
            //network.displayLinks();
            //network.displayNodes();

            Display d = new Display(network, size, degree, keysPerNode, amountOfNodesToCompromise, amountOfKeys, polynomialsOrder, amountOfNodesToCompromise);
            Graphics g = d.getGraphics();
            int m = 1;
            while(m< network.getNodes().size()) {
                System.out.println();
                System.out.println("            Attacker attack the network           ");
                System.out.println("--------------------------------------------------");
                System.out.println();

                int amountOfCompromisedLinks = Attacker.compromiseNetwork_Polynomial_Scheme(m, network);
                System.out.println("Resilience for " + m + " nodes compromised, with "
                        + keysPerNode + " polynomials each : " + ((double) amountOfCompromisedLinks / amountOfLinks));



                //Attacker.compromiseNodes(10, network);
                //network.displayNodes(NodeState.compromised);
                pr.println(m+";"+((double) amountOfCompromisedLinks / amountOfLinks));

                d.setNetwork(network);
                d.setAmountOfNodesToCompromise(m);
                d.paint(g);

                m++;
            }
            d.setAmountOfNodesToCompromise(m);
            network.displayPolynomialPool();
            pr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }*/


    }

}
