package com.dkkk.soothsayer.ui.library;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dkkk.soothsayer.R;
import com.dkkk.soothsayer.model.Article;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Адаптер для отображения библиотеки статей в RecyclerView.
 *
 * <p>
 * Формирует сложный список, состоящий из:
 * <ul>
 *     <li>разделителей категорий</li>
 *     <li>названий категорий</li>
 *     <li>элементов статей</li>
 * </ul>
 * </p>
 *
 * <p>
 * Используется для визуальной группировки статей по категориям.
 * </p>
 *
 * <p>
 * Типы элементов:
 * <ul>
 *     <li>TYPE_CATEGORY — строка категории</li>
 *     <li>TYPE_ARTICLE — объект статьи</li>
 *     <li>TYPE_DIVIDER — визуальный разделитель</li>
 * </ul>
 * </p>
 */
public class LibraryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /** Тип элемента: категория */
    private static final int TYPE_CATEGORY = 0;

    /** Тип элемента: статья */
    private static final int TYPE_ARTICLE = 1;

    /** Тип элемента: разделитель */
    private static final int TYPE_DIVIDER = 2;

    /**
     * Список элементов адаптера.
     *
     * <p>
     * Содержит:
     * <ul>
     *     <li>String (категории и DIVIDER)</li>
     *     <li>Article (статьи)</li>
     * </ul>
     * </p>
     */
    private final List<Object> items = new ArrayList<>();

    /** Обработчик кликов по статьям */
    private final OnArticleClick listener;

    /**
     * Интерфейс обработки кликов по статье.
     *
     * <p>
     * Используется для передачи события нажатия
     * в Activity или ViewModel слой.
     * </p>
     */
    public interface OnArticleClick {

        /**
         * Вызывается при выборе статьи пользователем.
         *
         * @param article выбранная статья
         */
        void onClick(Article article);
    }

    /**
     * Создаёт адаптер библиотеки.
     *
     * @param listener обработчик кликов по статьям
     */
    public LibraryAdapter(OnArticleClick listener) {
        this.listener = listener;
    }

    /**
     * Устанавливает данные в адаптер.
     *
     * <p>
     * Выполняет группировку статей по категориям и формирует
     * внутренний список элементов для RecyclerView.
     * </p>
     *
     * @param grouped карта категорий и соответствующих статей
     */
    public void setData(Map<String, List<Article>> grouped) {

        items.clear();

        for (String category : grouped.keySet()) {

            // визуальный разделитель перед категорией
            items.add("DIVIDER");

            // название категории
            items.add(category);

            List<Article> list = grouped.get(category);

            // добавление статей категории
            for (Article a : list) {
                items.add(a);
            }
        }

        notifyDataSetChanged();
    }

    /**
     * Определяет тип элемента по позиции.
     *
     * @param position индекс элемента
     * @return тип элемента RecyclerView
     */
    @Override
    public int getItemViewType(int position) {

        Object item = items.get(position);

        if (item instanceof String && item.equals("DIVIDER"))
            return TYPE_DIVIDER;

        if (item instanceof String)
            return TYPE_CATEGORY;

        return TYPE_ARTICLE;
    }

    /**
     * Создаёт ViewHolder в зависимости от типа элемента.
     *
     * @param parent родительский контейнер
     * @param viewType тип элемента
     * @return ViewHolder соответствующего типа
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_DIVIDER) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_divider, parent, false);
            return new DividerVH(v);
        }

        if (viewType == TYPE_CATEGORY) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_category, parent, false);
            return new CategoryVH(v);
        }

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_article, parent, false);

        return new ArticleVH(v);
    }

    /**
     * Привязывает данные к ViewHolder.
     *
     * @param holder ViewHolder элемента
     * @param position позиция в списке
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Object item = items.get(position);

        if (holder instanceof DividerVH) return;

        if (holder instanceof CategoryVH) {
            ((CategoryVH) holder).title.setText((String) item);
            return;
        }

        if (holder instanceof ArticleVH) {

            Article a = (Article) item;

            ((ArticleVH) holder).title.setText(a.title);

            holder.itemView.setOnClickListener(v -> listener.onClick(a));
        }
    }

    /**
     * Возвращает общее количество элементов в списке.
     *
     * @return размер списка
     */
    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * ViewHolder для категории.
     */
    static class CategoryVH extends RecyclerView.ViewHolder {

        /** Заголовок категории */
        TextView title;

        CategoryVH(View v) {
            super(v);
            title = v.findViewById(R.id.categoryTitle);
        }
    }

    /**
     * ViewHolder для статьи.
     */
    static class ArticleVH extends RecyclerView.ViewHolder {

        /** Заголовок статьи */
        TextView title;

        ArticleVH(View v) {
            super(v);
            title = v.findViewById(R.id.articleTitle);
        }
    }

    /**
     * ViewHolder для разделителя.
     */
    static class DividerVH extends RecyclerView.ViewHolder {

        DividerVH(View v) {
            super(v);
        }
    }
}