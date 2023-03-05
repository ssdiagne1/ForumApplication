/*
 * Main Activity
 * Samba Diagne
 */

package edu.uncc.hw07;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements SignUpFragment.SignUpListener, LoginFragment.LoginListener
        , ForumsFragment.ForumsListener, CreateForumFragment.CreateForumListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.rootView, new LoginFragment())
                .commit();
    }

    @Override
    public void login() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new LoginFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToCreateForum() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new CreateForumFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToDetailForm(Forums forum) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, ForumFragment.newInstance(forum))
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void goToForumsFragment(String id) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, ForumsFragment.newInstance(id))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void createNewAccount() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new SignUpFragment())
                .addToBackStack(null)
                .commit();
    }



}