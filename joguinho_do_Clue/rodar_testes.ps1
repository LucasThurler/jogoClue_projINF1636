Write-Host "Compilando..."
javac -cp "lib/*" -d bin "src/model/Dado.java" "src/model/Carta.java" "src/model/TipoCarta.java" "src/model/Jogador.java" "src/model/Baralho.java" "src/model/Jogo.java" "test/model/DadoTest.java" "test/model/BaralhoTest.java" "test/model/JogadorTest.java" "test/model/JogoTest.java"

Write-Host ""
Write-Host "=== DadoTest ==="
java -cp "bin;lib/*" org.junit.runner.JUnitCore model.DadoTest

Write-Host ""
Write-Host "=== BaralhoTest ==="
java -cp "bin;lib/*" org.junit.runner.JUnitCore model.BaralhoTest

Write-Host ""
Write-Host "=== JogadorTest ==="
java -cp "bin;lib/*" org.junit.runner.JUnitCore model.JogadorTest

Write-Host ""
Write-Host "=== JogoTest ==="
java -cp "bin;lib/*" org.junit.runner.JUnitCore model.JogoTest