module riscv {
	exports datapath;
	exports assembler;
	exports GUI;

	requires java.logging;
	requires javafx.base;
	requires javafx.controls;
	requires javafx.fxml;
	requires transitive javafx.graphics;
	requires richtextfx.fat;
	opens GUI to javafx.fxml;
}