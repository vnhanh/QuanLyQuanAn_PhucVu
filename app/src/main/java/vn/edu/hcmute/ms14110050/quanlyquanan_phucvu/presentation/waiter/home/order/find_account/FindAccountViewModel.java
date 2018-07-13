package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.home.order.find_account;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.adapters.TextViewBindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.viewmodel.BaseNetworkViewModel;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.Constant;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.databinding.BindableFieldTarget;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.picasso.RectangleImageTransform;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.picasso.ScaleType;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.sharedpreferences.SSharedReference;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.FindAccountResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.TypeAcc;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.account.AccountRequestManager;

public class FindAccountViewModel extends BaseNetworkViewModel<FindAccountView>
        implements GetCallback<FindAccountResponse>{

    public final ObservableBoolean showInfo = new ObservableBoolean(false);
    public final ObservableField<String> resultSearch = new ObservableField<>();

    public final ObservableField<Drawable> profileDrawable = new ObservableField<>();
    private BindableFieldTarget profileTarget;

    private AccountRequestManager accountRM;

    private String token;
    private String userName;
    private String myUserName;

    public FindAccountViewModel() {
        accountRM = AccountRequestManager.getInstance();
    }

    @Override
    public void onViewAttach(@NonNull FindAccountView viewCallback) {
        super.onViewAttach(viewCallback);

        profileTarget = new BindableFieldTarget(profileDrawable, getView().getContext().getResources());
    }

    public void onSubmitUserName(String username) {
        String input = username.trim().toLowerCase();
        if (input.equals("")) {
            return;
        }
        if (myUserName != null && myUserName.equals(username)) {
            showMessage(R.string.err_your_username, Constant.COLOR_ERROR);
            return;
        }
        showProgress(R.string.msg_finding_account);
        getToken();
        accountRM.findAccount(token, username, TypeAcc.WAITER, this);
    }

    public boolean isMyUserName() {
        return myUserName != null && myUserName.equals(currentInput);
    }

    private void getToken() {
        if (token == null) {
            token = SSharedReference.getToken(getContext());
        }
    }

    // Khi get được response tìm tài khoản từ server
    @Override
    public void onFinish(FindAccountResponse response) {
        if (response != null && response.isSuccess()) {
            showInfo.set(true);

            userName = response.getUsername();

            showProfileImage(response.getProfileUrlImg());

            StringBuilder builder = new StringBuilder();
            builder.append(response.getFullname()+"\n");
            builder.append(getString(R.string.label_birthdate) + " " + convertDate(response.getBirthdate())+"\n");
            builder.append(getString(R.string.label_gender) + " " + convertGender(response.isGender()) +"\n");

            resultSearch.set(builder.toString());
            hideProgress();
        }
    }

    private void showProfileImage(String profileUrlImg) {
        int size = getContext().getResources().getDimensionPixelSize(R.dimen.size_profile_img_find_account);
        int corner = getContext().getResources().getDimensionPixelSize(R.dimen.normal_space);
        int bgColor = ContextCompat.getColor(getContext(), R.color.colorWhite);

        RectangleImageTransform transform =
                new RectangleImageTransform(size, size, corner, ScaleType.CENTER_INSIDE);
        transform.setBackgroundColor(bgColor);

        Picasso.get()
                .load(profileUrlImg)
                .placeholder(R.drawable.ic_account_120dp_0dp)
                .error(R.drawable.ic_account_120dp_0dp)
                .transform(transform).into(profileTarget);
    }

    private String convertGender(boolean gender) {
        return getString(gender ? R.string.male : R.string.female);
    }

    private String convertDate(String input) {
        DateFormat fromDate = new SimpleDateFormat("yyyy-MM-dd");
        fromDate.setLenient(true);
        DateFormat toDate = new SimpleDateFormat("dd/MM/yyyy");
        toDate.setLenient(true);
        try {
            Date date = fromDate.parse(input);
            return toDate.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("LOG", getClass().getSimpleName() + ":getBirthDateStrValue():exception:" + e.getMessage());
            return "";
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setMyUserName(String myUserName) {
        this.myUserName = myUserName;
    }

    public String getMyUserName() {
        return myUserName;
    }

    private String currentInput;

    public void onUserNameChanged(CharSequence s) {
        currentInput = s.toString();
    }

    public String getCurrentInput() {
        return currentInput;
    }
}
