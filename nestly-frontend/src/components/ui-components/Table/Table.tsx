import styles from "./Table.module.scss"
import type { TableProps} from "./Table.types"

const Table = ({ children }:TableProps) => {
  return (
    <table className={styles.Table}>{children}</table>
  )
}

Table.TableHead = ({ children }: TableProps) => {
  return (
    <thead className={styles.TableHead}>{children}</thead>
  )
}

Table.Th = ({ children }: TableProps) => {
  return (
    <th className={styles.Th}>{children}</th>
  )
}

Table.TableBody = ({ children }: TableProps) => {
  return (
    <tbody className={styles.TableBody}>{children}</tbody>
  )
}

Table.TableRow = ({ children }: TableProps) => {
  return (
    <tr className={styles.TableRow}>{children}</tr>
  )
}

Table.TData = ({ children }: TableProps) => {
  return (
    <td className={styles.TData}>{children}</td>
    )
}


export default Table;