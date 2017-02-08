package jp.ne.home.jcom.kfujita.testapp002;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.server.converter.StringToIntConverter;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    NumberPicker numberPicker;
    NumberPicker numberPicker2;
    NumberPicker numberPicker3;
    NumberPicker numberPicker4;
    Button button;
    Button button2;
    ViewGroup tableLayout;
    String answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        generateAnswer();
        System.out.println(answer);
        findViews();
        initViews();

        final List<InputHitBlow> inputs = new ArrayList<>();

        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(String.valueOf(numberPicker.getValue()));
                stringBuilder.append(String.valueOf(numberPicker2.getValue()));
                stringBuilder.append(String.valueOf(numberPicker3.getValue()));
                stringBuilder.append(String.valueOf(numberPicker4.getValue()));
                InputHitBlow inputHitBlow = InputHitBlow.hit(stringBuilder.toString(), answer);
                inputs.add(inputHitBlow);

                getLayoutInflater().inflate(R.layout.table_row, tableLayout);

                int i = 0;
                for (InputHitBlow input : inputs) {
                    TableRow tableRow = (TableRow) tableLayout.getChildAt(i++);
                    ((TextView) tableRow.getChildAt(0)).setText(input.input);
                    ((TextView) tableRow.getChildAt(1)).setText(input.hit);
                    ((TextView) tableRow.getChildAt(2)).setText(input.blow);
                }
                if(Integer.valueOf(inputHitBlow.hit) == 4) {
                    endViews();
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void endViews() {
        button.setVisibility(View.INVISIBLE);
        button2.setVisibility(View.VISIBLE);
    }

    private void generateAnswer() {
        SecureRandom sha1PRNG = null;
        byte[] bytes = new byte[1];
        try {
            sha1PRNG = SecureRandom.getInstance("SHA1PRNG");
            sha1PRNG.nextBytes(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        List<Integer> integers = new ArrayList<>();
        while (integers.size() < 4) {
            int integer = (int) (10 * sha1PRNG.nextDouble());
            if(! integers.contains(integer)) {
                integers.add(integer);
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(integers.get(0));
        stringBuilder.append(integers.get(1));
        stringBuilder.append(integers.get(2));
        stringBuilder.append(integers.get(3));
        answer = stringBuilder.toString();
    }

    private void initViews() {
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(9);
        numberPicker2.setMinValue(0);
        numberPicker2.setMaxValue(9);
        numberPicker3.setMinValue(0);
        numberPicker3.setMaxValue(9);
        numberPicker4.setMinValue(0);
        numberPicker4.setMaxValue(9);
        button2.setVisibility(View.INVISIBLE);
    }

    private void findViews() {
        numberPicker = (NumberPicker) findViewById(R.id.numberPicker);
        numberPicker2 = (NumberPicker) findViewById(R.id.numberPicker2);
        numberPicker3 = (NumberPicker) findViewById(R.id.numberPicker3);
        numberPicker4 = (NumberPicker) findViewById(R.id.numberPicker4);
        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
        tableLayout = (ViewGroup) findViewById(R.id.table);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private static class InputHitBlow {
        String input;
        String hit;
        String blow;

        static InputHitBlow hit(String input, String answer) {
            int hit = 0;
            int blow = 0;
            for(int i = 0; i < 4; i++) {
                if (answer.contains(input.subSequence(i, 1 + i))) {
                    blow++;
                }
                if (input.charAt(i) == answer.charAt(i)) {
                    hit++;
                    blow--;
                }
            }
            InputHitBlow inputHitBlow = new InputHitBlow();
            inputHitBlow.input = input;
            inputHitBlow.hit = String.valueOf(hit);
            inputHitBlow.blow = String.valueOf(blow);
            return inputHitBlow;
        }
    }
}
