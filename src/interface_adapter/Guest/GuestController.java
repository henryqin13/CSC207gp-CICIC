package interface_adapter.Guest;

import interface_adapter.ViewManagerModel;
import use_case.Guest.GuestInputData;
import use_case.Guest.GuestInputBoundary;
import use_case.Signup.SignupInputBoundary;
import use_case.Signup.SignupInputData;

public class GuestController {

    final GuestInputBoundary guestUseCaseInteractor;
    final ViewManagerModel viewManagerModel;
    public GuestController(GuestInputBoundary guestUseCaseInteractor, ViewManagerModel viewManagerModel) {
        this.guestUseCaseInteractor = guestUseCaseInteractor;
        this.viewManagerModel = viewManagerModel;
    }


    public void execute() {
        GuestInputData guestInputData = new GuestInputData();

        guestUseCaseInteractor.execute(guestInputData);
    }
    public void exit(){
        viewManagerModel.setActiveView("the city game");
        viewManagerModel.firePropertyChanged();
    }
}
