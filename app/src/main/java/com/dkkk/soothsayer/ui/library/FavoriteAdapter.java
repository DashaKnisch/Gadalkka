package com.dkkk.soothsayer.ui.library;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.dkkk.soothsayer.R;
import com.dkkk.soothsayer.model.Article;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView адаптер для отображения списка избранных статей.
 *
 * <p>
 * Отвечает за:
 * <ul>
 *     <li>Отображение карточек избранных статей</li>
 *     <li>Обработку открытия статьи</li>
 *     <li>Удаление статьи из избранного</li>
 * </ul>
 * </p>
 *
 * <p>
 * Используется в FavoriteActivity.
 * </p>
 */
public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.VH> {

    /**
     * Интерфейс обработки действий пользователя с элементом списка.
     */
    public interface Listener {

        /**
         * Открытие выбранной статьи.
         *
         * @param article выбранная статья
         */
        void onOpen(Article article);

        /**
         * Удаление статьи из избранного.
         *
         * @param article статья для удаления
         */
        void onDelete(Article article);
    }

    /** Слушатель действий пользователя */
    private final Listener listener;

    /** Список избранных статей */
    private final List<Article> list = new ArrayList<>();

    /**
     * Создание адаптера.
     *
     * @param listener обработчик действий пользователя
     */
    public FavoriteAdapter(Listener listener) {
        this.listener = listener;
    }

    /**
     * Обновляет список избранных статей.
     *
     * @param data новый список статей
     */
    public void setData(List<Article> data) {
        list.clear();
        list.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * Создание ViewHolder для элемента списка.
     *
     * @param parent родительский контейнер
     * @param viewType тип элемента (не используется)
     * @return ViewHolder карточки статьи
     */
    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite_article, parent, false);

        return new VH(v);
    }

    /**
     * Привязка данных статьи к UI элементам.
     *
     * @param h ViewHolder элемента
     * @param position позиция в списке
     */
    @Override
    public void onBindViewHolder(VH h, int position) {

        Article a = list.get(position);

        h.title.setText(a.title);
        h.category.setText("Категория: " + a.category);
        h.author.setText("Автор: " + a.author);
        h.date.setText("Дата публикации: " + a.date);

        /**
         * Открытие статьи по нажатию на заголовок.
         */
        h.title.setOnClickListener(v -> listener.onOpen(a));

        /**
         * Удаление статьи из избранного.
         */
        h.btnDelete.setOnClickListener(v -> listener.onDelete(a));
    }

    /**
     * Количество элементов в списке.
     *
     * @return размер списка избранных статей
     */
    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * ViewHolder для элемента избранной статьи.
     *
     * <p>Содержит UI элементы карточки статьи.</p>
     */
    static class VH extends RecyclerView.ViewHolder {

        /** Заголовок статьи */
        TextView title;

        /** Категория статьи */
        TextView category;

        /** Автор статьи */
        TextView author;

        /** Дата публикации */
        TextView date;

        /** Кнопка удаления из избранного */
        MaterialButton btnDelete;

        /**
         * Инициализация UI элементов карточки.
         *
         * @param itemView корневой View элемента
         */
        public VH(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.favTitle);
            category = itemView.findViewById(R.id.favCategory);
            author = itemView.findViewById(R.id.favAuthor);
            date = itemView.findViewById(R.id.favDate);
            btnDelete = itemView.findViewById(R.id.btnDeleteFavorite);
        }
    }
}