export function formatDate(dateStr: string) {
  return new Intl.DateTimeFormat('pt-BR').format(new Date(dateStr + 'T00:00:00'));
}
