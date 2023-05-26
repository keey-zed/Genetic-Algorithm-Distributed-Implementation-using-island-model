package ma.enset.sma;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import ma.enset.seq.GAUtils;
import ma.enset.seq.Individual;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class IslandAgent extends Agent {
    private Individual[] population = new Individual[GAUtils.POPULATION_SIZE];
    private Individual firstIndividual;
    private Individual secondIndividual;

    @Override
    protected void setup() {
        SequentialBehaviour sequentialBehaviour = new SequentialBehaviour();
        sequentialBehaviour.addSubBehaviour(
                new OneShotBehaviour() {
                    @Override
                    public void action() {
                        initialize();
                        sortPopulation();
                    }
                }
        );
        sequentialBehaviour.addSubBehaviour(
                new Behaviour() {
                    int iteration = 0;
                    @Override
                    public void action() {
                        crossover();
                        mutation();
                        sortPopulation();
                        iteration ++;
                    }

                    @Override
                    public boolean done() {
                        return GAUtils.MAX_ITERATIONS == iteration && getBestFitness() == GAUtils.CHROMOSOME_SIZE;
                    }
                }
        );
        sequentialBehaviour.addSubBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                DFAgentDescription dfAgentDescription = new DFAgentDescription();
                ServiceDescription serviceDescription = new ServiceDescription();
                serviceDescription.setType("GeneticAlgorithm");
                DFAgentDescription[] dfAgentDescriptions;
                try {
                    dfAgentDescriptions = DFService.search(getAgent(), dfAgentDescription);
                } catch (FIPAException e) {
                    throw new RuntimeException(e);
                }
                dfAgentDescription.addServices(serviceDescription);
                ACLMessage aclMessage = new ACLMessage(ACLMessage.INFORM);
                aclMessage.addReceiver(dfAgentDescriptions[0].getName());
                aclMessage.setContent(Arrays.toString(population[0].getChromosome()) + " : " + String.valueOf(population[0].getFitness()));
                send(aclMessage);
            }
        });
        addBehaviour(sequentialBehaviour);
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            throw new RuntimeException(e);
        }
    }
    public void initialize() {
        for (int i = 0; i < GAUtils.POPULATION_SIZE ; i++) {
            population[i] = new Individual();
            population[i].calculateFitness();
        }
    }
    public void crossover() {
        firstIndividual = new Individual(population[0].getChromosome());
        secondIndividual = new Individual(population[1].getChromosome());
        Random random = new Random();
        int crossPoint = random.nextInt(GAUtils.CHROMOSOME_SIZE - 1);
        crossPoint++;
        for (int i = 0; i < crossPoint; i++) {
            firstIndividual.getChromosome()[i] = population[1].getChromosome()[i];
            secondIndividual.getChromosome()[i] = population[0].getChromosome()[i];
        }
    }
    public void showPopulation() {
        for (Individual individual : population) {
            System.out.println(Arrays.toString(individual.getChromosome()) + " = " + individual.getFitness());
        }
    }
    public void sortPopulation() {
        Arrays.sort(population, Comparator.reverseOrder());
    }
    public void mutation() {
        Random random = new Random();
        if(random.nextDouble() > GAUtils.MUTATION_PROB){
            int index = random.nextInt(GAUtils.CHROMOSOME_SIZE);
            firstIndividual.getChromosome()[index] = GAUtils.OPTIONS.charAt(random.nextInt(GAUtils.OPTIONS.length()));
        }
        if(random.nextDouble() > GAUtils.MUTATION_PROB){
            int index = random.nextInt(GAUtils.CHROMOSOME_SIZE);
            secondIndividual.getChromosome()[index] = GAUtils.OPTIONS.charAt(random.nextInt(GAUtils.OPTIONS.length()));
        }
        firstIndividual.calculateFitness();
        secondIndividual.calculateFitness();
        population[GAUtils.POPULATION_SIZE - 2] = firstIndividual;
        population[GAUtils.POPULATION_SIZE - 1] = secondIndividual;
    }
    public int getBestFitness() {
        return population[0].getFitness();
    }
}
