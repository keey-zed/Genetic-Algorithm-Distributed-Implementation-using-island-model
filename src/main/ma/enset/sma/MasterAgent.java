package ma.enset.sma;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

public class MasterAgent extends Agent {
    @Override
    protected void setup() {
        DFAgentDescription dfAgentDescription = new DFAgentDescription();
        dfAgentDescription.setName(getAID());
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setName("Master");
        serviceDescription.setType("GeneticAlgorithm");
        dfAgentDescription.addServices(serviceDescription);
        try {
            DFService.register(this, dfAgentDescription);
        } catch (FIPAException e) {
            throw new RuntimeException(e);
        }
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage received = receive();
                if (received != null) {
                    System.out.println(received.getSender().getName() + " : " + received.getContent());
                } else {
                    block();
                }
            }
        });
    }
}
