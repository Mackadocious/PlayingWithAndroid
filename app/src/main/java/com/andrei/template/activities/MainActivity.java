package com.andrei.template.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.andrei.template.BaseActivity;
import com.andrei.template.BaseApplication;
import com.andrei.template.R;
import com.andrei.template.data.models.Note;
import com.andrei.template.data.models.Note_;
import com.andrei.template.utils.DialogFactory;
import com.socks.library.KLog;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.Query;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MainActivity extends BaseActivity {

  @BindView(R.id.button_dialog) Button button_dialog;
  @BindView(R.id.button_snackbar) Button button_snackbar;
  @BindView(R.id.relayout_main) RelativeLayout relayout_main;


  private MainActivity mContext;
  private Box<Note> notesBox;
  private Query<Note> notesQuery;
  private BoxStore boxStore;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    getSupportActionBar().setElevation(0);
    mContext = MainActivity.this;

    //example writing and reading from the database
    boxStore = ((BaseApplication) getApplication()).getBoxStore();
    notesBox = boxStore.boxFor(Note.class);
    addNote();
    listNotes();


  }

  private void listNotes() {
    notesQuery = notesBox.query().order(Note_.text).build();
    List<Note> notes = notesQuery.find();
    for (Note n : notes) {
      KLog.d("Note: " + n.toString());
    }
  }


  private void addNote() {
    String noteText = UUID.randomUUID().toString();

    final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
    String comment = "Added on " + df.format(new Date());

    Note note = new Note(0, noteText, comment, new Date());
    notesBox.put(note);

    KLog.d("Inserted new note, ID: " + note.getId());
  }

  @OnClick(R.id.button_dialog) public void onClick_button_dialog() {

    DialogFactory.success_toast(mContext,"All is good!").show();

  }

  @OnClick(R.id.button_snackbar) public void onClick_button_snackbar() {


  }




  //---------- Menu Items ----------
  @Override public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    // Handle item selection
    switch (item.getItemId()) {
      case R.id.action_settings:
        startActivity(new Intent(mContext, SettingsActivity.class));
        return true;
      case R.id.action_exit:
        finish();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  //delete me if not needed... [remember to enable the library from build.gradle (app) file
  //private void rate_this_app_logic() {
  //  AppRate.with(this).setInstallDays(10).setLaunchTimes(10).setRemindInterval(2).setShowLaterButton(false).setDebug(false).monitor();
  //  AppRate.showRateDialogIfMeetsConditions(this);
  //}
}


