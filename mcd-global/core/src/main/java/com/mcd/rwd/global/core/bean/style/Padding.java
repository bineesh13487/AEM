package com.mcd.rwd.global.core.bean.style;

/**
 * Created by Rakesh.Balaiah on 21-03-2016.
 */
public class Padding {

	private final double left;

	private final double right;

	private final double top;

	private final double bottom;

	private Unit unit;

	private Padding(double top, double right, double bottom, double left, Unit unit) {
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.left = left;
		this.unit = unit;
	}

	@Override public String toString() {
		return "padding: " + convertValue(this.top) + " " + convertValue(this.right) + " " + convertValue(
				this.bottom) + " " + convertValue(this.left) + ";";
	}

	public final String getPaddingLeft() {
		return "padding-left: " + convertValue(this.left) + ";";
	}

	public final String getPaddingRight() {
		return "padding-right: " + convertValue(this.right) + ";";
	}

	public final String getPaddingTop() {
		return "padding-top: " + convertValue(this.top) + ";";
	}

	public final String getPaddingBottom() {
		return "padding-bottom: " + convertValue(this.bottom) + ";";
	}

	private final String convertValue(double value) {
		if (this.unit == Unit.PIXELS) {
			return (int) value + this.unit.getValue();
		}
		return value + this.unit.getValue();
	}

	public static class PaddingBuilder {

		private double left;

		private double right;

		private double top;

		private double bottom;

		private Unit unit;

		public PaddingBuilder(Unit unit) {
			this.unit = unit;
		}

		public Padding.PaddingBuilder top(final double top) {
			this.top = top;
			return this;
		}

		public Padding.PaddingBuilder right(final double right) {
			this.right = right;
			return this;
		}

		public Padding.PaddingBuilder bottom(final double bottom) {
			this.bottom = bottom;
			return this;
		}

		public Padding.PaddingBuilder left(final double left) {
			this.left = left;
			return this;
		}

		public Padding createPadding() {
			return new Padding(top, right, bottom, left, unit);
		}
	}
}
