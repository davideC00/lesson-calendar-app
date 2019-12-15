package it.uniba.di.sms.orariolezioni.ui.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.Calendar;
import java.util.Date;

public class PagerViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private Date date;
    private Date rightDate;
    private Date leftDate;
    private boolean switchState;

    public PagerViewModel() {
        mText = new MutableLiveData<>();
        setCentralDate(new Date());
        mText.setValue("This is home fragment");
        switchState = true;
    }

    public LiveData<String> getText() {
        return mText;
    }

    public Date getCentralDate() {
        return date;
    }

    public void setCentralDate(Date date) {
        this.date = date;
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        // Add the date for the central's left page of central
        c.add(Calendar.DATE, -1);
        setLeftDate(c.getTime());

        // Add the date for the central's right page of central
        c.add(Calendar.DATE, 2);
        setRightDate(c.getTime());
    }

    public Date getRightDate() {
        return rightDate;
    }

    public void setRightDate(Date rightDate) {
        this.rightDate = rightDate;
    }

    public Date getLeftDate() {
        return leftDate;
    }

    public void setLeftDate(Date leftDate) {
        this.leftDate = leftDate;
    }


    public boolean getSwitchState() {
        return switchState;
    }

    public void setSwitchState(boolean switchState) {
        this.switchState = switchState;
    }
}