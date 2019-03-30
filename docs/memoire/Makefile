TEX_FILES=main.tex formalism.tex
TEX_COMPILER=pdflatex
BIB_COMPILER=bibtex

 main.pdf: $(TEX_FILES)
	$(TEX_COMPILER) $(TEX_FILES)
	$(BIB_COMPILER) $(TEX_FILES)
	$(TEX_COMPILER) $(TEX_FILES)
	$(TEX_COMPILER) $(TEX_FILES)

.PHONY: clean

clean:
	rm -f *.aux *.bbl *.blg *.log *.out *.toc