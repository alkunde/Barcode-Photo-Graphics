package br.com.mobiletkbrazil.avaliacao;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class RelatorioActivity extends AppCompatActivity implements OnClickListener {

    private DataPoint[] dados;
    private GraphView graph;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio);

        dados = new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        };

        graph = findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dados);
        graph.addSeries(series);
        findViewById(R.id.bt_novos_dados).setOnClickListener(this);
    }

    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_novos_dados:
                graph.removeAllSeries();
                /*dados = new DataPoint[]{
                        new DataPoint(1, 0),
                        new DataPoint(2, 7),
                        new DataPoint(3, 4),
                        new DataPoint(4, 6),
                        new DataPoint(5, 2),
                        new DataPoint(6, 1),
                        new DataPoint(7, 5)
                };
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dados);
                graph.addSeries(series);
                Toast.makeText(getApplicationContext(), "Gerar novos dados", Toast.LENGTH_SHORT).show();*/
                DataPoint[] points = new DataPoint[100];
                for (int i = 0; i < points.length; i++) {
                    points[i] = new DataPoint(i, Math.sin(i*0.5) * 20*(Math.random()*10+1));
                }
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);

                // set manual X bounds
                graph.getViewport().setYAxisBoundsManual(true);
                graph.getViewport().setMinY(-150);
                graph.getViewport().setMaxY(150);

                graph.getViewport().setXAxisBoundsManual(true);
                graph.getViewport().setMinX(4);
                graph.getViewport().setMaxX(80);

                // enable scaling and scrolling
                graph.getViewport().setScalable(true);
                graph.getViewport().setScalableY(true);

                graph.addSeries(series);

                //graph.getViewport().setScrollable(true); // enables horizontal scrolling
                //graph.getViewport().setScrollableY(true); // enables vertical scrolling
                //graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
                //graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling
                break;
        }
    }

}
