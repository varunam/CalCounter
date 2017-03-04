package app.calcounterapplication.com.calcounter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import android.os.Handler;

import data.CustomListViewAdapter;
import data.DatabaseHandler;
import model.Food;
import util.Utils;

public class DisplayFoodsActivity extends AppCompatActivity {

    private DatabaseHandler dba;
    private ArrayList<Food> dbFoods = new ArrayList<>();
    private ListView foodList;
    private CustomListViewAdapter foodAdapter;
    private Food myFood;
    private TextView totalCals, totalFoods;
    private Button addItem;
    int doubleBacktoExitPressedOnce = 0;

    @Override
    public void onBackPressed() {
        if(doubleBacktoExitPressedOnce == 1)
        {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }

        this.doubleBacktoExitPressedOnce++;
        if(doubleBacktoExitPressedOnce<2)
        Toast.makeText(getApplicationContext(), "Press back again to exit!", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                doubleBacktoExitPressedOnce = 0;
            }
        }, 4000);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_foods);

        totalCals   = (TextView) findViewById(R.id.totalAmountTextView);
        totalFoods = (TextView) findViewById(R.id.totalItemsTextView);
        foodList = (ListView) findViewById(R.id.list);
        addItem = (Button) findViewById(R.id.addItem);

        refreshData();

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DisplayFoodsActivity.this, MainActivity.class));
            }
        });



    }

    private void refreshData() {

        dbFoods.clear();

        dba = new DatabaseHandler(getApplicationContext());
        ArrayList<Food> foodsFromDB = dba.getFood();

        int calsValue = dba.totalCalories();
        int totalItems = dba.getTotalItems();

        String formattedValue = Utils.formatNumber(calsValue);
        String formattedItems = Utils.formatNumber(totalItems);

        totalCals.setText("Total Calories: " + formattedValue);
        totalFoods.setText("Total Foods: " + formattedItems);

        for (int i=0; i< foodsFromDB.size(); i++)
        {
            String name = foodsFromDB.get(i).getFoodName();
            String date = foodsFromDB.get(i).getRecordDate();
            int cals = foodsFromDB.get(i).getCalories();
            int foodId = foodsFromDB.get(i).getFoodId();

            Log.v("FOOD IDS: ", String.valueOf(foodId));

            myFood = new Food();
            myFood.setFoodName(name);
            myFood.setCalories(cals);
            myFood.setFoodId(foodId);
            myFood.setRecordDate(date);

            dbFoods.add(myFood);
        }

        dba.close();

        //setup Adapter
        foodAdapter = new CustomListViewAdapter(DisplayFoodsActivity.this, R.layout.list_row, dbFoods);
        foodList.setAdapter(foodAdapter);
        foodAdapter.notifyDataSetChanged();

    }
}
