# Algorytmy rozwiązujące problem QAP
Michał Kuźma i Michał Biernacki  
11 listopada 2016  







## Opis problemu

QAP (Quadratic assignment problem) reprezentuje następujące zadanie:

*Dane są zbiory n lokalizacji i n ośrodków. Każda para lokacji znajduje się w określonej odległości od siebie, a dla każdej pary ośrodków znany jest przepływ. Celem jest takie przypisanie ośrodków do lokalizacji, aby zminimalizować sumę iloczynów odległości i przepływów.*

Problem wykorzystywany jest często do zamodelowania zadania rozmieszczenia fabryk (ośrodków) w zestawie znanych lokalizacji. Jako przepływy podane są wówczas interakcje, w jakie fabryki wchodzą wzajemnie (transport surowców, etc.).

Ponieważ problem należy do grupy NP-trudnych, nie jest znany algorytm, który pozwoliłby na znalezienie dokładnego rozwiązania w czasie wielomianowym. W celu osiągnięcia zadowalających wyników czasowych uzyskując dobre rozwiązanie, wykorzystuje się algorytmy heurystyczne i metaheurystyki.

## Operator sąsiedztwa

W projekcie korzystano z operatora sąsiedztwa 2-OPT, który dla każdej permutacji zwraca sąsiedztwo złożone ze wszystkich permutacji uzyskanych przez zamianę dwóch pozycji miejscami.

Wykorzystanie tego operatora sprawia, że wielkość sąsiedztwa każdej permutacji wynosi n<sup>2</sup> (gdzie n to długość permutacji).

## Krótki opis zaimplementowanych algorytmów

### Random Search

Algorytm przeszukiwania losowego (Random Search) jest najprostszym z wykorzystanych w projekcie. Przez określony czas losuje on rozwiązania i na koniec zwraca najlepsze z nich. Parametr czasowy stanowi jedyne kryterium stopu algorytmu.

### Local Search

Algorytm przeszukiwania lokalnego (Local Search) wyszukuje lepsze rozwiązania w zbiorach sąsiedztwa aż do osiągnięcia optimum lokalnego. Nie daje jednak żadnej gwarancji odnalezienia optimum globalnego. Algorytm został zaimplementowany w dwóch wersjach różniących się sposobem wyboru sąsiada, do którego algorytm powinien przejść.

- *Greedy Local Search* wybiera pierwszego sąsiada, który jest lepszy od obecnie rozpatrywanego rozwiązania.
- *Steepest Local Search* przeszukuje całe sąsiedztwo wybierając najlepszego sąsiada i przechodzi do niego, jeśli jest lepszy od obecnego rozwiązania.

Przeprowadzono eksperyment zliczający, ilu sąsiadów oceniają obie wersje algorytmu, oraz ile kroków robią. Wyniki przedstawiono na poniższych wykresach. W celu lepszej czytelności wykresów, pominięto wyniki dwóch największych instancji.

![](sprawozdanie_files/figure-html/gs_compare-1.png)<!-- -->![](sprawozdanie_files/figure-html/gs_compare-2.png)<!-- -->

Powyższe wykresy pokazują, że w pełnej iteracji obie wersje oceniają zbliżoną liczbę rozwiązań, jednak zazwyczaj *Steepest* ocenia ich więcej. Eksperyment pokazał również, że algorytm *Steepest*, który dokładnie wybiera zawsze najlepsze kolejne rozwiązanie wykonuje w pełnej iteracji więcej kroków (dla każdej badanej instancji problemu).

### Heurystyka

Algorytm heurystyczny buduje rozwiązanie w oparciu o predefiniowane reguły, które zgodnie z założeniem mają prowadzić do dobrego wyniku. Zapproponowana w projekcie heurystyka działa analogicznie do sortowania bąbelkowego. Po wylosowaniu permutacji startowej wybierany jest najkorzystniejszy w obecnym krajobrazie element na kolejne pozycje (0, 1, 2, ...).

```
  solution = randomSolution()
  value = solution.getValue()
  
  for (i = 0; i < n; i++) {
    for (j = i + 1; j < n; j++) {
      changeValue = valueOfChangingItemsAtPositions(i, j)
      if (changeValue < 0) {
        changeElementsAtPositions(i, j)
        value += changeValue
      }
    }
  }
```

## Porównanie odległości od optimum rozwiązań uzyskanych przez badane algorytmy

Do określenia odległości od optimum globalnego wykorzystano miarę opisaną wzorem:

$dist = \frac{Opt_{L} - Opt_{G}}{Opt_{G}}$

Dla każdej badanej instancji wykonano 10 pomiarów. Porównano wyniki średnie, oraz najlepsze. Wyniki pomiarów zostały przedstawione na poniższych wykresach.

![](sprawozdanie_files/figure-html/results_compare-1.png)<!-- -->

Zaobserwować można wysoką niestabilność uzyskanych wyników dla małych instancji problemu (duże odchylenia standardowe dla każdego algorytmu). Istotny jest również charakter malejący przedstawionych relacji. Dla większywch instancji problemu odległość względna od optimum jest bardzo niewielka. 


![](sprawozdanie_files/figure-html/best_results_compare-1.png)<!-- -->

Po rozpatrzeniu najlepszego zamiast średniego rozwiązania, charakter malejący jest już słabiej zauważalny. Najmniejsze odległości występują dla instancji najmniejszych i największych, podczas gdy instancje o pośredniej wielkości mają największą względną odległość od optimum.

Algorytmy *Greedy* i *Steepest* osiągają bardzo podobne średnie rozwiązania. Zaproponowana heurystyka zwraca nieznacznie gorsze rozwiązania od algorytmów przeszukiwania lokalnego, natomiast *Random* osiąga rozwiązania daleko gorsze od pozostałych.

Warty podkreślenia jest jeszcze wyraźnie banalny charakter instancji problemu o rozmiarze 26 (wszystkie algorytmy osiągnęły rozwiązania bardzo bliskie optimum globalnemu).

## Porównanie czasów wykonywania algorytmów

![](sprawozdanie_files/figure-html/times_compare-1.png)<!-- -->

![](sprawozdanie_files/figure-html/best_times_compare-1.png)<!-- -->
