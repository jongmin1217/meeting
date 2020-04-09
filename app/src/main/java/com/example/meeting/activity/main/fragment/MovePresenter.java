package com.example.meeting.activity.main.fragment;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import com.example.meeting.model.MoveData;
import com.example.meeting.recyclerview.MoveAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MovePresenter {

    private MoveView view;
    private MoveAdapter adapter;
    private MoveHandler handler;
    private ArrayList<MoveData> dataList;

    public MovePresenter(MoveView view,MoveAdapter adapter){
        this.view = view;
        this.adapter = adapter;
        handler = new MoveHandler();
    }

    public void getMoveInfo(){
        view.showProgress();
        Thread t = new Thread(() -> {
            Document doc = null;
            try {
                dataList = new ArrayList<>();
                doc = Jsoup.connect("https://movie.naver.com/movie/running/current.nhn").get();
                Elements mElementDataSize = doc.select("ul[class=lst_detail_t1]").select("li");
                for (Element elem : mElementDataSize) {
                    MoveData data = new MoveData();
                    String title = elem.select("li dt[class=tit] a").text();
                    String age = elem.select("li dt[class=tit] span").text();
                    String star = elem.select("li dd[class=star] dl[class=info_star] span[class=num]").text();
                    String exp = elem.select("li dd[class=star] dl[class=info_exp] span[class=num]").text()+"%";
                    String link = "https://movie.naver.com/movie/running/current.nhn" + elem.select("li div[class=thumb] a").attr("href");
                    String imgUrl = elem.select("li div[class=thumb] a img").attr("src");
                    Element rElem = elem.select("dl[class=info_txt1] dt").next().first();
                    String release = rElem.select("dd").text();
                    Element dElem = elem.select("dt[class=tit_t2]").next().first();
                    String director = dElem.select("a").text();
                    Element aElem = elem.select("dt[class=tit_t3]").next().first();
                    String actor;
                    try {
                        actor = aElem.select("a").text();
                    } catch (NullPointerException e) {
                        actor = "";
                    }

                    data.setTitle(title);
                    data.setAge(age);
                    data.setStar(star);
                    data.setExp(exp);
                    data.setLink(link);
                    data.setImgUrl(imgUrl);
                    data.setRelease(release);
                    data.setDirector(director);
                    data.setActor(actor);

                    dataList.add(data);
                }

                handler.sendEmptyMessage(0);


            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        t.start();
    }

    @SuppressLint("HandlerLeak")
    class MoveHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    adapter.getItem(dataList);
                    break;
            }
        }
    }
}
