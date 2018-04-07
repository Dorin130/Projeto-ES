package pt.ulisboa.tecnico.softeng.broker.domain;


import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.CarInterface;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class ReserveVehicleState extends AdventureState{
    public static final int MAX_REMOTE_ERRORS = 5;

    @Override
    public State getState() {
        return State.RESERVE_VEHICLE;
    }

    @Override
    public void process(Adventure adventure) {
        try {
            String nif = adventure.getClient().getNIF();
            String iban = adventure.getClient().getIBAN();
            String drivingLicense = adventure.getClient().getDRIVINGLICENSE();
            adventure.setVehicleConfirmation(
                    CarInterface.processVehicleRenting(drivingLicense, adventure.getBegin(), adventure.getEnd(),nif, iban));
        } catch (CarException ce) {
            adventure.setState(State.UNDO);
            return;
        } catch (RemoteAccessException rae) {
            incNumOfRemoteErrors();
            if (getNumOfRemoteErrors() == MAX_REMOTE_ERRORS) {
                adventure.setState(State.UNDO);
            }
            return;
        }
        adventure.setState(State.PROCESS_PAYMENT);
    }
}
