package com.dev.nguyenvantung.fg_app.ui.splash;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.nguyenvantung.fg_app.R;
import com.dev.nguyenvantung.fg_app.data.model.login.LoginRequesst;
import com.dev.nguyenvantung.fg_app.data.repository.AuthRepository;
import com.dev.nguyenvantung.fg_app.data.source.remote.AuthRemoteDataSource;
import com.dev.nguyenvantung.fg_app.data.source.remote.response.login.LoginResponse;
import com.dev.nguyenvantung.fg_app.ui.login.LoginActivity;
import com.dev.nguyenvantung.fg_app.ui.main.MainActivity;
import com.dev.nguyenvantung.fg_app.utils.AppConstants;
import com.dev.nguyenvantung.fg_app.utils.AppPref;
import com.dev.nguyenvantung.fg_app.utils.navigator.Navigator;
import com.dev.nguyenvantung.fg_app.utils.rx.SchedulerProvider;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity implements SplashContract.View{
    private static final String TAG = SplashActivity.class.getName();
    private static final int SPLASH_DISPLAY_LENGHT = 1000;

    @BindView(R.id.splah_version)
    public TextView txtVersion;

    @BindView(R.id.splash_logo_layout)
    public CardView mCardView;

    @BindView(R.id.splash_img_logo)
    public ImageView imgLogo;

    private AppPref mAppPref;
    private SplashContract.Presenter mPresenter;
    private Navigator mNavigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        mAppPref = AppPref.getInstance(this);
        mNavigator = new Navigator(this);

        initViews();
        initPresenter();
        checkLogin();
    }

    private void initViews() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.animation_logo_splash);
        imgLogo.startAnimation(animation);

        mCardView.setTranslationZ(10);

        //set version
        try {
            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            txtVersion.setText(getString(R.string.version) + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initPresenter() {
        AuthRepository authRepository = new AuthRepository(AuthRemoteDataSource.getInstance(this));
        mPresenter = new SplashPresenter(authRepository, SchedulerProvider.getInstance());
        mPresenter.setView(this);
    }

    private void checkLogin() {
        if (mAppPref.getRemember() && !mAppPref.getEmail().isEmpty() && !mAppPref.getPassword().isEmpty()){
            mPresenter.login(new LoginRequesst(mAppPref.getEmail(), mAppPref.getPassword(), mAppPref.getRemember()));
        }else {
            new Handler().postDelayed(() -> nextToLoginActivity(), SPLASH_DISPLAY_LENGHT);
        }
    }

    private void nextToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        mNavigator.startActivity(intent, mCardView, getString(R.string.share_view));
        finish();
    }

    private void nextToMain(){
        mNavigator.startActivity(MainActivity.class);
        finish();
    }

    @Override
    public void loginSuccess(LoginResponse mLoginResponse) {
        AppPref appPref = AppPref.getInstance(this);
        appPref.putApiToken(mLoginResponse.getAccessToken());
        appPref.putEmail(appPref.getEmail());
        appPref.putPassword(appPref.getPassword());
        appPref.putRememberMe(appPref.getRemember());
        AppConstants.USER = mLoginResponse.getUser();
        nextToMain();
    }

    @Override
    public void loginError() {
        nextToLoginActivity();
    }
}
