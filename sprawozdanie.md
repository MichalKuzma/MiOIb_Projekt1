# Algorytmy rozwiązujące problem QAP
Michał‚ Kuźma i Michał‚ Biernacki  
5 grudnia 2016  







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

Algorytm heurystyczny buduje rozwiązanie w oparciu o predefiniowane reguły, które zgodnie z założeniem mają prowadzić do dobrego wyniku.Zaproponowana w projekcie heurystyka działa na bardzo naiwnym założeniu, że każdy element w permutacji ma swoją jedną, najbardziej optymalną pozycję (w tej permutacji), i zawsze przesuwając ten element bliżej tego miejsca zwiększamy wartość funkcji celu (zmniejszamy koszta). W takim przypadku problem ten sprowadza się do problemu sortowania. Heurystyka rozwiązuje to dalej stosując podejście analogiczne, jak algorytm sortowania bąbelkowego, dzięki czemu złożoność obliczeniowa wynosi O(n<sup>2</sup>), gdzie n to rozmiar permutacji.
Początkowa permutacja jest tworzona w sposób losowy, dzięki czemu heurystyka może każdorazowo dla jednej instancji problemu zwracać różne wyniki.

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

### Simulated Annealing

Algorytm symulowanego wyżarzania (simulated annealing) przeszukuje przestrzeń rozwiązań poprzez przechodzenie do losowego sąsiada, który poprawia, a przynajmniej za mocno nie pogarsza wartości funkcji celu. To czy algorytm przejdzie do pogarszającego sąsiada zależy od parametru temperatury. Początkowo ustawiana jest taka wartość temperatury, by 95% losowych sąsiadów było akceptowanych, a następnie, co określoną ilość kroków ([wielkość instancji]<sub>2</sub>) jest on zmniejszany (w naszym zastosowaniu, jest temperatura mnożona razy 0.95, bo taka wartość dawała najlepsze rezultaty w stosunku do czasu przetwarzania).
Algorytm kończy swoje działanie, gdy nie znajdzie akceptowalnego sąsiada w określonej liczbie prób ([wielkość instancji]<sub>2</sub>), lub gdy temperatura osiągnie taką wartość, że prawdopodobieństwo akceptacji ruchów pogarszających będzie wynosiło 0%.

### Tabu Search

Algorytm przeszukiwania tabu (tabu search) w każdym etapie wybiera listę kilku najlepszych możliwych ruchów (o rozmiarze [wielkość instancji]/10). Dalej z tej listy wybierane są kolejno najlepsze ruchy w stosunku do aktualnego stanu, aż lista nie zostanie pusta. Ruch odrzucany jest na 2 sposoby:

- Gdy strata jest zbyt duża, w tym z czasem działania akceptowalna strata jest coraz mniejsza. Analogicznie jak temperatura w algorytmie symulowanego wyżarzania.
- Gdy ruch znajduje się na liście tabu, akceptowany jest tylko jeżeli polepsza wartość funkcji celu.

Lista tabu ma na celu zablokowanie "cofania" się - są na niej ruchy przeciwne (takie które przywróciłyby stan) do ruchów ostatnio wykonanych. W naszej implementacji ma ona rozmiar nie większy niż [wielkość instancji]/4. Po wykonaniu każdego ruchu, ruch do niego przeciwny jest dodawany do listy, a najstarszy na liście jest odrzucany.
Algorytm kończy swoje działanie w podobny sposób jak algorytm symulowanego wyżarzania - gdy przez dużą liczbę operacji ([wielkość instancji]<sub>2</sub>) nie zostanie wykonany żaden ruch, lub gdy prawdopodobieństwo akceptacji ruchów pogarszających będzie wynosiło 0%.


## Porównanie odległości od optimum rozwiązań uzyskanych przez badane algorytmy

Do określenia odległości od optimum globalnego wykorzystano miarę opisaną wzorem:

$dist = \frac{Opt_{L} - Opt_{G}}{Opt_{G}}$

Dla każdej badanej instancji wykonano 10 pomiarów. Porównano wyniki średnie oraz najlepsze. Wyniki pomiarów zostały przedstawione na poniższych wykresach.

![](sprawozdanie_files/figure-html/results_compare-1.png)<!-- -->![](sprawozdanie_files/figure-html/results_compare-2.png)<!-- -->

![](sprawozdanie_files/figure-html/best_results_compare-1.png)<!-- -->![](sprawozdanie_files/figure-html/best_results_compare-2.png)<!-- -->

Ze względu na diametralnie gorsze wyniki, dla lepszej czytelności, wykres algorytmu *Random Search* przedstawiono na odrębnej skali.

Zaobserwować można wysoką niestabilność uzyskanych wyników dla niektórych instancji problemu (duże odchylenia standardowe dla każdego algorytmu). Może to być spowodowane skomplikowaną przestrzenią rozwiązań. 

Wszystkie badane algorytmy zdają się wykazywać niezależność jakości rozwiązania końcowego (odległości od optimum) od wielkości instancji. Różnice w jakości końcowego rozwiązania powodowane są raczej różnym stopniem skomplikowania powierzchni rozwiązań dla danych instancji.

Algorytmy *Greedy* i *Steepest* osiągają bardzo podobne średnie rozwiązania. Zaproponowana heurystyka zwraca nieznacznie gorsze rozwiązania od algorytmów przeszukiwania lokalnego, natomiast *Random* osiąga rozwiązania daleko gorsze od pozostałych (poza prostymi instancjami).

Algorytmy Simulated Annealing (SA) i Tabu Search (TS) wykazują większą niezależność od stopnia skomplikowania instancji (mniejsze odchylenia od wyliczonej funkcji liniowej), szczególnie uwzględniając rozwiązanie najlepsze ze znalezionych

## Porównanie czasów wykonywania algorytmów

Ponieważ warunkiem stopu algorytmu *Random* jest upłynięcie określonego czasu (średniego czasu wykonywania *Local Search*), jego wykresy zostały pominięte.

![](sprawozdanie_files/figure-html/times_compare-1.png)<!-- -->

![](sprawozdanie_files/figure-html/best_times_compare-1.png)<!-- -->

Przedstawione powyżej wykresy czasu trwania iteracji algorytmów pokazują wielomianowy wzrost czasu trwania algorytmu wraz ze wzrostem wielkości instancji (czego można się było spodziewać). Dla algorytmów *SA* i *TS* czas rośnie znacznie szybciej od pozostałych (wielomian wyższego stopnia).

Wykresy średniego czasu działania pokazują rosnącą niestabilność mierzonego czasu (która nie jest jednak zależna jedynie od wielkości instancji). Najmniejsze odchylenia zanotowano dla algorytmu *Steepest LS*, co było do przewidzenia. W każdym kroku oceni on taką samą liczbę sąsiadów (co zajmie tyle samo czasu), więc czas trwania algorytmu zależy wyłącznie od liczby wykonanych kroków. Ponieważ dla danej instancji wykonuje on zawsze mniej więcej tyle samo kroków (potwierdzają to niewielkie odchylenia standardowe na wykresie rozmiar instancji / liczba kroków), liczba odwiedzonych sąsiadów, a więc i czas trwania algorytmu cechują się wysoką stabilnością.

Trudno jednoznacznie określić, który z algorytmów przeszukiwania lokalnego działa szybciej. Dla niektórych instancji krótszy czas osiągnął *Steepest*, dla innych *Greedy*. Jest to silnie związane z kształtem przestrzeni rozwiązań, a nie bezpośrednio z wielkością instancji.

## Efektywność algorytmów

Przeprowadzono analizę efektywności algorytmów. Miarę efektywności opracowano według założenia, że najwyższą efektywność ma algorytm, który zwraca rozwiązanie optymalne w czasie zerowym. Miarę efektywności ograniczono do przedziału $[0, 1]$ według poniższego wzoru:

$efectiveness = \frac{1}{(dist + 1) * (time / 60000 + 1)}$

![](sprawozdanie_files/figure-html/efectiveness_compare-1.png)<!-- -->![](sprawozdanie_files/figure-html/efectiveness_compare-2.png)<!-- -->

Pierwszą obserwacją jest wyraźnie gorsza efektywność algorytmu przeszukiwania losowego. Algorytmy *SA* i *TS*, a szczególnie ten pierwszy, wykazują większą efektywność dla wielu instancji testowych niż przeszukiwanie lokalne, jednak przez szybciej rosnący czas, ich efektywność spada dla większych instancji. Algorytm symulowanego wyżarzania cechuje się najmniejszą zmiennością efektywności w zależności od instancji problemu (najmniejsze odchylenia od funkcji uzyskanej z regresji liniowej).

## Zależność jakości rozwiązania końcowego od jakości rozwiązania początkowego (algorytmy przeszukiwania lokalnego)

Przeprowadzono eksperyment porównujący jakość rozwiązania początkowego z jakością rozwiązania końcowego w algorytmach przeszukiwania lokalnego. Wyniki zilustrowano poniższymi wykresami.

![](sprawozdanie_files/figure-html/init_result_relation-1.png)<!-- -->![](sprawozdanie_files/figure-html/init_result_relation-2.png)<!-- -->![](sprawozdanie_files/figure-html/init_result_relation-3.png)<!-- -->

Z powyższych wykresów wynika, że nie istnieje wyraźna zależność między jakością rozwiązania początkowego, a końcowego. Pokazują one natomiast charakter badanych instancji problemu, w których określone rozwiązania występują częściej od innych (poziome "linie" na wykresach).

Brak zależności między jakością początkowego i końcowego rozwiązania wynika ze złożonego kształtu powierzchni rozwiązań. Algorytmy przeszukiwania lokalnego znajdują lokalne optimum, a nie mamy żadnej gwarancji, że w okolicach dobrego rozwiązania będzie dobre optimum lokalne. Jedyną gwarancją daną nam przez algorytmy przeszukiwania lokalnego jest fakt, że rozwiązanie końcowe będzie zawsze nie gorsze od początkowego.

## Multi-random local search: Zależność uzyskanego rozwiązania od liczby restartów

Dla dwóch algorytmów przeszukiwania lokalnego (*Greedy* i *Steepest*) przeprowadzono eksperyment sprawdzający zależność jakości uzyskanego rozwiązania od ilości restartów (ponownych przeszukiwań lokalnych zaczynających od losowo wybranych punktów początkowych). Wyniki przedstawiono na poniższych wykresach.

![](sprawozdanie_files/figure-html/multi_random-1.png)<!-- -->![](sprawozdanie_files/figure-html/multi_random-2.png)<!-- -->

Zamieszczone wykresy pokazują, że algorytmy stosunkowo szybko (już po 2 -- 3 iteracjach) osiągają rozwiązanie bliskie końcowemu (gdzie za końcowe  uznajemy takie, które nie zmienia się przez kilkadziesiąt iteracji). Dalsze iteracje pomagają natomiast w coraz wolniejszym tempie poprawiać uzyskane rozwiązanie. Algorytm *Steepest* szybciej osiąga końcowe rozwiązanie. Sugeruje to, że efektywniej eksploruje on przestrzeń. Wybieranie za każdym razem najlepszego sąsiada może więc prowadzić do większej różnorodności znajdowanych optimów lokalnych po wielokrotnym uruchomieniu algorytmu. Więcej znalezionych optimów lokalnych zwiększa natomiast prawdopodobieństwo znalezienia optimum globalnego.

## Ocena podobieństwa znajdywanych rozwiązań lokalnie optymalnych

Określono podobieństwo między rozwiązaniami znalezionymi przez algorytmy przeszukiwania lokalnego. Podobieństwo zostało zdefiniowane, jako liczba pozycji, na których rozwiązania mają równe wartości. Przykładowo:


Permutacja 1                Permutacja 2                 Podobieństwo
--------------------------  --------------------------  -------------
2 1 4 5 11 10 6 0 9 7 3 8   2 9 4 1 11 10 6 0 7 5 3 8               8
8 3 4 0 6 11 1 5 2 9 10 7   8 3 4 0 1 5 11 9 2 6 10 7               7
7 9 1 10 11 6 4 0 2 5 3 8   2 9 4 1 11 10 6 0 7 5 3 8               6
7 9 1 10 11 6 4 0 2 5 3 8   7 9 1 10 11 4 6 5 2 0 3 8               8
7 9 1 10 11 4 6 5 2 0 3 8   7 2 6 9 11 4 10 5 1 0 3 8               7

Sporządzono wykresy próbując zbadać zależność między podobieństwem, a różnicą w jakości dla par rozwiązań. Badanie przeprowadzono na dwóch niewielkich instancjach.

![](sprawozdanie_files/figure-html/results_similarity_graphs-1.png)<!-- -->![](sprawozdanie_files/figure-html/results_similarity_graphs-2.png)<!-- -->

Dla obu instancji problemu zaobserwowano zależność między podobieństwem rozwiązań, a różnicą ich jakości. Dla rozwiązań różniących się znacznie od siebie, różnica jakości wydaje się być losowa (występują zarówno pary o podobnej jakości, jak i bardzo odległe od siebie). Natomiast rozwiązania podobne do siebie częściej mają niewielką róźnicę jakości.

## Wnioski

W ramach zadania zaimplementowano i przetestowano 6 algorytmów rozwiązujących problem QAP: *Random Search*, *Greedy Local Search*, *Steepest Local Search*, *Simulated Annealing*, *Tabu Search* oraz autorski algorytm heurystyczny. Do przeszukiwania przestrzeni rozwiązań skorzystano z sąsiedztwa 2-OPT, które dla każdego rozwiązania zwraca n<sup>2</sup> jego sąsiadów. 

W pierwszej kolejności porównano liczbę wykonanych kroków oraz sprawdzonych sąsiadów przez algorytmy *Greedy LS* i *Steepest LS*. Okazało się, że jakkolwiek *Steepest* wykonuje znacznie mniej kroków dla każdej badanej instancji, liczba sprawdzonych przez niego sąsiadów w pełnej iteracji jest zazwyczaj wyższa od "konkurenta".

Względna odległość znalezionego rozwiązania od optimum globalnego jest niezależna od wielkości instancji. Różnice w jakości rozwiązania wynikają raczej z "trudności" instancji problemu - stopnia skomplikowania przestrzeni rozwiązań.

Czas wykonywania algorytmów rośnie wraz ze wzrostem wielkości instancji. Jest to spodziewany wniosek, jednak należy zauważyć, że od tej reguły są wyjątki i długość permutacji nie jest jedynym kryterium determinującym czas wykonywania algorytmu. Czas wykonywania algorytmu dla wszystkich testowanych algorytmów zdaje się rosnąć wielomianowo, jednak znacznie szybciej dla algorytmów *Simulated Annealing* i *Tabu Search* (wyższy stopień wielomianu).

Najbardziej stabilny czas wykonywania wykazuje *Steepest Local Search*, co jest zrozumiałe biorąc pod uwagę, że w każdym kroku przeszukuje całe dostępne sąsiedztwo (które ma równą liczność w każdym punkcie przestrzeni rozwiązań).

TODO: Opisać niestabilność czasu wykonywania *SA* i *TS*.

Porównanie czasu wykonywania algorytmów *Greedy* i *Steepest* prowadzi do wniosku, że żaden z nich nie jest jednoznacznie lepszy od drugiego. Zawsze znajdzie się instancja, dla której *Greedy* zakończy się szybciej oraz taka, dla której to *Steepest* osiągnie lepszy czas.

Zaproponowano miarę efektywności iteracji algorytmów. Uzależniono ją od czasu trwania iteracji i jakości znalezionego rozwiązania (odległości od rozwiązania optymalnego). Uzyskane wyniki pokazują lepsze wyniki algorytmów *SA* i *TS*, jednak dla dużych instancji problemu, znaczny czas wykonywania prowadzi do niskiej oceny efektywności.

Nie można wskazać wyrażnej zależności między jakością rozwiązania początkowego, a końcowego w algorytmach przeszukiwania lokalnego. Jedyną pewną zależnością jest, że zwrócone rozwiązanie będzie się charakteryzowało jakością nie gorszą od początkowego.

*Multi-random Local Search* (wielokrotne uruchamianie przeszukiwania lokalnego w losowych punktach) stanowi bardzo sprawne ulepszenie algorytmów LS. Z przeprowadzonych eksperymentów wynika, że dobre rozwiązanie osiągane jest stosunkowo szybko. Wielokrotne uruchamianie *LS* pozwala na znalezienie większej liczby optimów lokalnych, a w konsekwencji zwiększa szansę znalezienia rozwiązania optymalnego globalnie.

Na koniec oceniono podobieństwo znajdowanych przez *LS* rozwiązań oraz podjęto próbę znalezienia relacji między podobieństwem rozwiązań, a różnicą ich jakości. Wykorzystano miarę równą liczbie pozycji, na których permutacje mają równe wartości. Zauważono, że dla niektórych instancji może występować wyraźna zależność między podobieństwem rozwiązań, a różnicą ich jakości.

## Napotkane trudności

Przy wykonywaniu zadania nie natrafiono na żadne istotne trudności.

