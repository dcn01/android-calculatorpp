/*
 * Copyright (c) 2009-2011. Created by serso aka se.solovyev.
 * For more information, please, contact se.solovyev@gmail.com
 */

package org.solovyev.android.calculator;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.widget.EditText;
import org.jetbrains.annotations.NotNull;
import org.solovyev.android.calculator.model.ParseException;
import org.solovyev.android.calculator.model.TextProcessor;

/**
 * User: serso
 * Date: 9/17/11
 * Time: 12:25 AM
 */
public class CalculatorEditor extends EditText {

	private boolean highlightText = true;

	@NotNull
	private final static TextProcessor<TextHighlighter.Result> textHighlighter = new TextHighlighter(Color.WHITE, false);

	public CalculatorEditor(Context context) {
		super(context);
	}

	public CalculatorEditor(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CalculatorEditor(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onCheckIsTextEditor() {
		return false;
	}

	@Override
	protected void onCreateContextMenu(ContextMenu menu) {
		super.onCreateContextMenu(menu);

		menu.removeItem(android.R.id.selectAll);
		menu.removeItem(android.R.id.startSelectingText);
	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		super.setText(text, type);
	}

	public synchronized void redraw() {
		String text = getText().toString();

		int selectionStart = getSelectionStart();
		int selectionEnd = getSelectionEnd();

		if (highlightText) {

			Log.d(this.getClass().getName(), text);

			try {
				final TextHighlighter.Result result = textHighlighter.process(text);
				selectionStart += result.getOffset();
				selectionEnd += result.getOffset();
				text = result.toString();
			} catch (ParseException e) {
				Log.e(this.getClass().getName(), e.getMessage(), e);
			}

			Log.d(this.getClass().getName(), text);
			super.setText(Html.fromHtml(text), BufferType.EDITABLE);
		} else {
			super.setText(text, BufferType.EDITABLE);
		}

		Log.d(this.getClass().getName(), getText().toString());
		setSelection(selectionStart, selectionEnd);
	}

	public boolean isHighlightText() {
		return highlightText;
	}

	public void setHighlightText(boolean highlightText) {
		this.highlightText = highlightText;
		redraw();
	}
}