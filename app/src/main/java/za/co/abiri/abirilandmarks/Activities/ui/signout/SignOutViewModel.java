package za.co.abiri.abirilandmarks.Activities.ui.signout;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SignOutViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public SignOutViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}