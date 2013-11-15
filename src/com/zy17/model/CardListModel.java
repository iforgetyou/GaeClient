package com.zy17.model;

import android.os.SystemClock;
import com.zy17.protobuf.domain.Eng;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@ThreadSafe
public class CardListModel {
	public interface Listener {
		void onModelStateUpdated(CardListModel cardListModel);
	}
	
    private Eng.CardList cardList;
	
	private final List<Listener> listeners = new ArrayList<Listener>();
	
	public CardListModel() {
		
	}

	public final Eng.CardList getData() {
//        获取从网络获取数据
		synchronized (this) {
			return cardList;
		}
	}
	
	public final void updateData() { // takes a while!
		synchronized (this) {
//			data = newData;
		}
		
		synchronized (listeners) {
			for (Listener listener : listeners) {
				listener.onModelStateUpdated(this);
			}
		}
	}
	
	public final void addListener(Listener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}
	
	public final void removeListener(Listener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}
}
