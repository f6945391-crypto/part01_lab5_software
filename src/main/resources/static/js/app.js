// Abre el modal de prediccion y lo precarga con el partido elegido.
// El guardado se hace por POST a /predicciones (la validacion real esta
// en el servidor; aqui solo preparamos el formulario).

const el = id => document.getElementById(id);

const prediccionModalEl = el('prediccionModal');
const prediccionModal = prediccionModalEl
    ? new bootstrap.Modal(prediccionModalEl)
    : null;

document.querySelectorAll('.btn-predecir').forEach(btn => {
  btn.addEventListener('click', () => {
    el('prediccionForm').reset();
    el('partidoId').value = btn.dataset.id;
    el('modalMatch').textContent =
        btn.dataset.local + ' vs ' + btn.dataset.visitante;
    el('labelLocal').textContent = 'Goles ' + btn.dataset.local;
    el('labelVisitante').textContent = 'Goles ' + btn.dataset.visitante;
    prediccionModal.show();
  });
});
