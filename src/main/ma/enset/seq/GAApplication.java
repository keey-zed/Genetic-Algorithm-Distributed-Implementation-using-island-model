package ma.enset.seq;
public class GAApplication {
    public static void main(String[] args) {
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();
        geneticAlgorithm.initialize();
        geneticAlgorithm.sortPopulation();
        geneticAlgorithm.showPopulation();
        int count = 0;
        while (GAUtils.MAX_ITERATIONS > count && geneticAlgorithm.getBestFitness() < GAUtils.CHROMOSOME_SIZE) {
            System.out.println("Iteration : " + count);
            geneticAlgorithm.crossover();
            geneticAlgorithm.mutation();
            geneticAlgorithm.sortPopulation();
            geneticAlgorithm.showPopulation();
            count ++;
        }

    }
}