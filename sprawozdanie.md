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

Przeprowadzono eksperyment zliczający, ilu sąsiadów oceniają obie wersje algorytmu, oraz ile kroków robią. Wyniki przedstawiono na poniższych wykresach. W celu lepszej czytelności wykresów, wybrano instancje o wielkości mniejszej, niż $n = 30$.

![](sprawozdanie_files/figure-html/gs_compare-1.png)<!-- -->![](sprawozdanie_files/figure-html/gs_compare-2.png)<!-- -->

Powyższe wykresy pokazują, że w pełnej iteracji *Steepest* wykonuje znacznie mniej kroków od algorytmu *Greedy*, jednak ponieważ za każdym razem sprawdza wszystkich możliwych sąsiadów, liczba ocenionych przez niego rozwiązań jest większa dla większości instancji problemu. Algorytm *Greedy* wykazuje dużą niestabilność zarówno w liczbie wykonanych kroków, jak i ocenionych sąsiadów (Dla większości instancji odchylenia są większe, niż w *Steepest*).

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

Zaobserwować można wysoką niestabilność uzyskanych wyników dla niektórych instancji problemu (duże odchylenia standardowe dla każdego algorytmu). Może to być spowodowane skomplikowaną powierzchnią rozwiązań.


![](sprawozdanie_files/figure-html/best_results_compare-1.png)<!-- -->

Po rozpatrzeniu najlepszego zamiast średniego rozwiązania, charakter malejący jest już słabiej zauważalny. Najmniejsze odległości występują dla instancji najmniejszych i największych, podczas gdy instancje o pośredniej wielkości mają największą względną odległość od optimum.

Algorytmy *Greedy* i *Steepest* osiągają bardzo podobne średnie rozwiązania. Zaproponowana heurystyka zwraca nieznacznie gorsze rozwiązania od algorytmów przeszukiwania lokalnego, natomiast *Random* osiąga rozwiązania daleko gorsze od pozostałych (poza prostymi instancjami).

Metodą regresji liniowej, wyznaczono zależności między wielkością instancji, a względną odległością rozwiązania od optimum. Algorytm *Random* zdaje się wykazywać delikatną zależność malejącą (im większa instancja, tym mniejsza względna odległość od optimum). Pozostałe algorytmy nie wykazują żadnej relacji między tymi wielkościami.

## Porównanie czasów wykonywania algorytmów

Ponieważ warunkiem stopu algorytmu *Random* jest upłynięcie określonego czasu (ściśle - średniego czasu wykonywania *Local Search*), jego wykresy zostały pominięte.

![](sprawozdanie_files/figure-html/times_compare-1.png)<!-- -->

![](sprawozdanie_files/figure-html/best_times_compare-1.png)<!-- -->

Przedstawione powyżej wykresy czasu trwania iteracji algorytmów pokazują wzrost czasu trwania algorytmu wraz ze wzrostem wielkości instancji (czego można się było spodziewać). Wykresy średniego czasu działania pokazują rosnącą niestabilność mierzonego czasu (która nie jest jednak zależna jedynie od wielkości instancji). Średni czas wykonywania algorytmu lokalnego przeszukiwania w obu wersjach (*Greedy* i *Steepest*) jest podobny dla każdej testowanej instancji problemu. Niektóre instancje rozwiązywane są szybciej przy użyciu jednego z nich, inne - drugiego. 

## Zależność jakości rozwiązania końcowego od jakości rozwiązania początkowego (algorytmy przeszukiwania lokalnego)

Przeprowadzono eksperyment porównujący jakość rozwiązania początkowego z jakością rozwiązania końcowego w algorytmach przeszukiwania lokalnego. Wyniki zilustrowano poniższymi wykresami.

![](sprawozdanie_files/figure-html/init_result_relation-1.png)<!-- -->![](sprawozdanie_files/figure-html/init_result_relation-2.png)<!-- -->![](sprawozdanie_files/figure-html/init_result_relation-3.png)<!-- -->

Z powyższych wykresów wynika, że nie istnieje wyraźna zależność między jakością rozwiązania początkowego, a końcowego. Pokazują one natomiast charakter badanych instancji problemu, w których określone rozwiązania występują częściej od innych (poziome "linie" na wykresach).

Brak zależności między jakością początkowego i końcowego rozwiązania wynika ze złożonego kształtu powierzchni rozwiązań. Algorytmy przeszukiwania lokalnego znajdują lokalne optimum, a nie mamy żadnej gwarancji, że w okolicach dobrego rozwiązania będzie dobre optimum lokalne. Jedyną gwarancją daną nam przez algorytmy przeszukiwania lokalnego jest fakt, że rozwiązanie końcowe będzie zawsze nie gorsze od początkowego.

## Multi-random local search: Zależność uzyskanego rozwiązania od liczby restartów

Dla dwóch algorytmów przeszukiwania lokalnego (*Greedy* i *Steepest*) przeprowadzono eksperyment sprawdzający zależność jakości uzyskanego rozwiązania od ilości restartów (ponownych przeszukiwań lokalnych zaczynających od losowo wybranych punktów początkowych). Wyniki przedstawiono na poniższych wykresach.

![](sprawozdanie_files/figure-html/multi_random-1.png)<!-- -->![](sprawozdanie_files/figure-html/multi_random-2.png)<!-- -->

Zamieszczone wykresy pokazują, że algorytmy stosunkowo szybko (już po 2 - 3 iteracjach) osiągają rozwiązanie bliskie końcowemu (gdzie za końcowe  uznajemy takie, które nie zmienia się przez kilkadziesiąt iteracji). Dalsze iteracje pomagają natomiast w coraz wolniejszym tempie poprawiać uzyskane rozwiązanie. Algorytm *Steepest* szybciej osiąga końcowe rozwiązanie. Sugeruje to, że efektywniej eksploruje on przestrzeń. Wybieranie za każdym razem najlepszego sąsiada może więc prowadzić do większej różnorodności znajdowanych optimów lokalnych po wielokrotnym uruchomieniu algorytmu. Więcej znalezionych optimów lokalnych zwiększa natomiast prawdopodobieństwo znalezienia optimum globalnego.

## Ocena podobieństwa znajdywanych rozwiązań lokalnie optymalnych

Poniżej znajdują się listy znalezionych rozwiązań dla dwóch instancji


### data/qapdata/had12

- 2 1 11 5 10 4 6 7 9 0 3 8
- 2 1 5 9 10 6 11 4 7 0 3 8
- 8 3 0 6 10 5 11 4 7 1 9 2
- 3 0 4 8 6 5 10 11 7 1 9 2
- 8 3 0 6 5 10 4 1 7 11 9 2
- 8 3 6 0 5 10 4 1 7 11 9 2
- 2 9 10 1 11 5 4 0 7 6 3 8
- 3 0 4 8 10 6 11 9 7 5 1 2
- 2 1 6 9 11 5 4 0 7 10 3 8

### data/qapdata/nug14

- 2 3 13 1 0 9 6 7 12 5 11 4 10 8
- 1 12 10 8 11 5 13 7 4 9 0 3 6 2
- 0 4 3 1 5 8 7 2 12 9 10 11 6 13
- 6 0 1 3 9 7 4 12 13 5 10 8 11 2
- 3 2 6 11 9 1 13 4 7 5 0 12 8 10
- 0 12 8 11 9 1 13 10 4 5 3 2 7 6
- 8 0 1 12 3 10 7 6 13 2 11 4 9 5
- 0 1 12 10 8 5 13 6 7 4 9 3 2 11
- 9 5 6 10 11 2 4 12 7 8 3 13 1 0
- 0 13 4 5 9 1 12 8 10 11 3 2 7 6
