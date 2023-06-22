# entry-sign-publisher

CSM30 - Desenvolvimento Integrado de Sistemas

Microsserviço responsável por receber um sinal de entrada para reconstrução de imagens de ultrassom.

Recebe via método **HTTP POST** um sinal em formato .csv faz a leitura deste arquivo e publica em uma fila(MESSAGE
BROKER) em formato _string_ o identificador do usuário e o conteúdo do sinal recebido.

___
