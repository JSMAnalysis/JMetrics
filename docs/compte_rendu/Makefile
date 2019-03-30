TEX_FILES=main.tex
TEX_COMPILER=pdflatex

 main.pdf: $(TEX_FILES)
	$(TEX_COMPILER) $(TEX_FILES)
	$(TEX_COMPILER) $(TEX_FILES)

.PHONY: clean

clean:
	rm -f *.aux *.bbl *.blg *.log *.out *.toc