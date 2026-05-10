Write-Host "Compilando..."
javac -cp "lib/*" -d bin "src/model/Dado.java" "src/model/Carta.java" "src/model/TipoCarta.java" "src/model/Jogador.java" "src/model/Baralho.java" "src/model/Jogo.java" "src/model/MapeaCasas.java" "src/model/DeslocarPiao.java" "test/model/DadoTest.java" "test/model/BaralhoTest.java" "test/model/JogadorTest.java" "test/model/JogoTest.java" "test/model/MapeaCasasTest.java" "test/model/DeslocarPiaoTest.java"

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

Write-Host ""
Write-Host "=== MapeaCasasTest ==="
java -cp "bin;lib/*" org.junit.runner.JUnitCore model.MapeaCasasTest

Write-Host ""
Write-Host "=== DeslocarPiaoTest ==="
java -cp "bin;lib/*" org.junit.runner.JUnitCore model.DeslocarPiaoTest