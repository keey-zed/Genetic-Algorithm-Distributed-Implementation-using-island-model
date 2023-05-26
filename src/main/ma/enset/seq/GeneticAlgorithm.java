package ma.enset.seq;
import java.util.*;
public class GeneticAlgorithm {
    private Individual[] population = new Individual[GAUtils.POPULATION_SIZE];
    private Individual firstIndividual;
    public Individual secondIndividual;
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
