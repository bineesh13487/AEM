package com.mcd.rwd.global.core.bean.style;

/**
 * Created by Rakesh.Balaiah on 21-03-2016.
 */
public class Margin {

	private final double left;

	private final double right;

	private final double top;

	private final double bottom;

	private Unit unit;

	private Margin(double top, double right, double bottom, double left, Unit unit) {
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.left = left;
		this.unit = unit;
	}

	@Override public String toString() {
		return "margin: " + convertValue(this.top) + " " + convertValue(this.right) + " " + convertValue(
				this.bottom) + " " + convertValue(this.left) + ";";
	}

	public final String getMarginLeft() {
		return "margin-left: " + convertValue(this.left) + ";";
	}

	public final String getMarginRight() {
		return "margin-right: " + convertValue(this.right) + ";";
	}

	public final String getMarginTop() {
		return "margin-top: " + convertValue(this.top) + ";";
	}

	public final String getMarginBottom() {
		return "margin-bottom: " + convertValue(this.bottom) + ";";
	}

	private final String convertValue(double value) {
		if (this.unit == Unit.PIXELS) {
			return (int) value + this.unit.getValue();
		}
		return value + this.unit.getValue();
	}

	public static class MarginBuilder {

		private double left;

		private double right;

		private double top;

		private double bottom;

		private Unit unit;

		public MarginBuilder(Unit unit) {
			this.unit = unit;
		}

		public MarginBuilder top(final double top) {
			this.top = top;
			return this;
		}

		public MarginBuilder right(final double right) {
			this.right = right;
			return this;
		}

		public MarginBuilder bottom(final double bottom) {
			this.bottom = bottom;
			return this;
		}

		public MarginBuilder left(final double left) {
			this.left = left;
			return this;
		}

		public Margin createMargin() {
			return new Margin(top, right, bottom, left, unit);
		}
	}
}
